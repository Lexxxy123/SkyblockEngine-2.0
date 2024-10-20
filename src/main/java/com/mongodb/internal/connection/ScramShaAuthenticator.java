/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.connection;

import com.mongodb.AuthenticationMechanism;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.internal.authentication.NativeAuthenticationHelper;
import com.mongodb.internal.authentication.SaslPrep;
import com.mongodb.internal.connection.MongoCredentialWithCache;
import com.mongodb.internal.connection.SaslAuthenticator;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;
import org.bson.internal.Base64;

class ScramShaAuthenticator
extends SaslAuthenticator {
    private final RandomStringGenerator randomStringGenerator;
    private final AuthenticationHashGenerator authenticationHashGenerator;
    private static final int MINIMUM_ITERATION_COUNT = 4096;
    private static final String GS2_HEADER = "n,,";
    private static final int RANDOM_LENGTH = 24;
    private static final byte[] INT_1 = new byte[]{0, 0, 0, 1};
    private static final AuthenticationHashGenerator DEFAULT_AUTHENTICATION_HASH_GENERATOR = new AuthenticationHashGenerator(){

        @Override
        public String generate(MongoCredential credential) {
            char[] password = credential.getPassword();
            if (password == null) {
                throw new IllegalArgumentException("Password must not be null");
            }
            return new String(password);
        }
    };
    private static final AuthenticationHashGenerator LEGACY_AUTHENTICATION_HASH_GENERATOR = new AuthenticationHashGenerator(){

        @Override
        public String generate(MongoCredential credential) {
            String username = credential.getUserName();
            char[] password = credential.getPassword();
            if (username == null || password == null) {
                throw new IllegalArgumentException("Username and password must not be null");
            }
            return NativeAuthenticationHelper.createAuthenticationHash(username, password);
        }
    };

    ScramShaAuthenticator(MongoCredentialWithCache credential) {
        this(credential, new DefaultRandomStringGenerator(), ScramShaAuthenticator.getAuthenicationHashGenerator(credential.getAuthenticationMechanism()));
    }

    ScramShaAuthenticator(MongoCredentialWithCache credential, RandomStringGenerator randomStringGenerator) {
        this(credential, randomStringGenerator, ScramShaAuthenticator.getAuthenicationHashGenerator(credential.getAuthenticationMechanism()));
    }

    ScramShaAuthenticator(MongoCredentialWithCache credential, RandomStringGenerator randomStringGenerator, AuthenticationHashGenerator authenticationHashGenerator) {
        super(credential);
        this.randomStringGenerator = randomStringGenerator;
        this.authenticationHashGenerator = authenticationHashGenerator;
    }

    @Override
    public String getMechanismName() {
        AuthenticationMechanism authMechanism = this.getMongoCredential().getAuthenticationMechanism();
        if (authMechanism == null) {
            throw new IllegalArgumentException("Authentication mechanism cannot be null");
        }
        return authMechanism.getMechanismName();
    }

    @Override
    protected SaslClient createSaslClient(ServerAddress serverAddress) {
        return new ScramShaSaslClient(this.getMongoCredentialWithCache(), this.randomStringGenerator, this.authenticationHashGenerator);
    }

    private static AuthenticationHashGenerator getAuthenicationHashGenerator(AuthenticationMechanism authenticationMechanism) {
        return authenticationMechanism == AuthenticationMechanism.SCRAM_SHA_1 ? LEGACY_AUTHENTICATION_HASH_GENERATOR : DEFAULT_AUTHENTICATION_HASH_GENERATOR;
    }

    private static class CacheValue {
        private byte[] clientKey;
        private byte[] serverKey;

        CacheValue(byte[] clientKey, byte[] serverKey) {
            this.clientKey = clientKey;
            this.serverKey = serverKey;
        }
    }

    private static class CacheKey {
        private final String hashedPasswordAndSalt;
        private final String salt;
        private final int iterationCount;

        CacheKey(String hashedPasswordAndSalt, String salt, int iterationCount) {
            this.hashedPasswordAndSalt = hashedPasswordAndSalt;
            this.salt = salt;
            this.iterationCount = iterationCount;
        }

        public boolean equals(Object o2) {
            if (this == o2) {
                return true;
            }
            if (o2 == null || this.getClass() != o2.getClass()) {
                return false;
            }
            CacheKey that = (CacheKey)o2;
            if (this.iterationCount != that.iterationCount) {
                return false;
            }
            if (!this.hashedPasswordAndSalt.equals(that.hashedPasswordAndSalt)) {
                return false;
            }
            return this.salt.equals(that.salt);
        }

        public int hashCode() {
            int result = this.hashedPasswordAndSalt.hashCode();
            result = 31 * result + this.salt.hashCode();
            result = 31 * result + this.iterationCount;
            return result;
        }
    }

    private static class DefaultRandomStringGenerator
    implements RandomStringGenerator {
        private DefaultRandomStringGenerator() {
        }

        @Override
        public String generate(int length) {
            SecureRandom random = new SecureRandom();
            int comma = 44;
            int low = 33;
            int high = 126;
            int range = high - low;
            char[] text = new char[length];
            for (int i2 = 0; i2 < length; ++i2) {
                int next = random.nextInt(range) + low;
                while (next == comma) {
                    next = random.nextInt(range) + low;
                }
                text[i2] = (char)next;
            }
            return new String(text);
        }
    }

    public static interface AuthenticationHashGenerator {
        public String generate(MongoCredential var1);
    }

    public static interface RandomStringGenerator {
        public String generate(int var1);
    }

    class ScramShaSaslClient
    implements SaslClient {
        private final MongoCredentialWithCache credential;
        private final RandomStringGenerator randomStringGenerator;
        private final AuthenticationHashGenerator authenticationHashGenerator;
        private final String hAlgorithm;
        private final String hmacAlgorithm;
        private String clientFirstMessageBare;
        private String clientNonce;
        private byte[] serverSignature;
        private int step = -1;

        ScramShaSaslClient(MongoCredentialWithCache credential, RandomStringGenerator randomStringGenerator, AuthenticationHashGenerator authenticationHashGenerator) {
            this.credential = credential;
            this.randomStringGenerator = randomStringGenerator;
            this.authenticationHashGenerator = authenticationHashGenerator;
            if (credential.getAuthenticationMechanism().equals((Object)AuthenticationMechanism.SCRAM_SHA_1)) {
                this.hAlgorithm = "SHA-1";
                this.hmacAlgorithm = "HmacSHA1";
            } else {
                this.hAlgorithm = "SHA-256";
                this.hmacAlgorithm = "HmacSHA256";
            }
        }

        @Override
        public String getMechanismName() {
            return this.credential.getAuthenticationMechanism().getMechanismName();
        }

        @Override
        public boolean hasInitialResponse() {
            return true;
        }

        @Override
        public byte[] evaluateChallenge(byte[] challenge) throws SaslException {
            ++this.step;
            if (this.step == 0) {
                return this.computeClientFirstMessage();
            }
            if (this.step == 1) {
                return this.computeClientFinalMessage(challenge);
            }
            if (this.step == 2) {
                return this.validateServerSignature(challenge);
            }
            throw new SaslException(String.format("Too many steps involved in the %s negotiation.", this.getMechanismName()));
        }

        private byte[] validateServerSignature(byte[] challenge) throws SaslException {
            String serverResponse = this.encodeUTF8(challenge);
            HashMap<String, String> map = this.parseServerResponse(serverResponse);
            if (!MessageDigest.isEqual(this.decodeBase64(map.get("v")), this.serverSignature)) {
                throw new SaslException("Server signature was invalid.");
            }
            return challenge;
        }

        @Override
        public boolean isComplete() {
            return this.step == 2;
        }

        @Override
        public byte[] unwrap(byte[] incoming, int offset, int len) {
            throw new UnsupportedOperationException("Not implemented yet!");
        }

        @Override
        public byte[] wrap(byte[] outgoing, int offset, int len) {
            throw new UnsupportedOperationException("Not implemented yet!");
        }

        @Override
        public Object getNegotiatedProperty(String propName) {
            throw new UnsupportedOperationException("Not implemented yet!");
        }

        @Override
        public void dispose() {
        }

        private byte[] computeClientFirstMessage() throws SaslException {
            String clientFirstMessage;
            this.clientNonce = this.randomStringGenerator.generate(24);
            this.clientFirstMessageBare = clientFirstMessage = "n=" + this.getUserName() + ",r=" + this.clientNonce;
            return this.decodeUTF8(ScramShaAuthenticator.GS2_HEADER + clientFirstMessage);
        }

        private byte[] computeClientFinalMessage(byte[] challenge) throws SaslException {
            String serverFirstMessage = this.encodeUTF8(challenge);
            HashMap<String, String> map = this.parseServerResponse(serverFirstMessage);
            String serverNonce = map.get("r");
            if (!serverNonce.startsWith(this.clientNonce)) {
                throw new SaslException("Server sent an invalid nonce.");
            }
            String salt = map.get("s");
            int iterationCount = Integer.parseInt(map.get("i"));
            if (iterationCount < 4096) {
                throw new SaslException("Invalid iteration count.");
            }
            String clientFinalMessageWithoutProof = "c=" + this.encodeBase64(ScramShaAuthenticator.GS2_HEADER) + ",r=" + serverNonce;
            String authMessage = this.clientFirstMessageBare + "," + serverFirstMessage + "," + clientFinalMessageWithoutProof;
            String clientFinalMessage = clientFinalMessageWithoutProof + ",p=" + this.getClientProof(this.getAuthenicationHash(), salt, iterationCount, authMessage);
            return this.decodeUTF8(clientFinalMessage);
        }

        String getClientProof(String password, String salt, int iterationCount, String authMessage) throws SaslException {
            String hashedPasswordAndSalt = this.encodeUTF8(this.h(this.decodeUTF8(password + salt)));
            CacheKey cacheKey = new CacheKey(hashedPasswordAndSalt, salt, iterationCount);
            CacheValue cachedKeys = ScramShaAuthenticator.this.getMongoCredentialWithCache().getFromCache(cacheKey, CacheValue.class);
            if (cachedKeys == null) {
                byte[] saltedPassword = this.hi(this.decodeUTF8(password), this.decodeBase64(salt), iterationCount);
                byte[] clientKey = this.hmac(saltedPassword, "Client Key");
                byte[] serverKey = this.hmac(saltedPassword, "Server Key");
                cachedKeys = new CacheValue(clientKey, serverKey);
                ScramShaAuthenticator.this.getMongoCredentialWithCache().putInCache(cacheKey, new CacheValue(clientKey, serverKey));
            }
            this.serverSignature = this.hmac(cachedKeys.serverKey, authMessage);
            byte[] storedKey = this.h(cachedKeys.clientKey);
            byte[] clientSignature = this.hmac(storedKey, authMessage);
            byte[] clientProof = this.xor(cachedKeys.clientKey, clientSignature);
            return this.encodeBase64(clientProof);
        }

        private byte[] decodeBase64(String str) {
            return Base64.decode(str);
        }

        private byte[] decodeUTF8(String str) throws SaslException {
            try {
                return str.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e2) {
                throw new SaslException("UTF-8 is not a supported encoding.", e2);
            }
        }

        private String encodeBase64(String str) throws SaslException {
            return Base64.encode(this.decodeUTF8(str));
        }

        private String encodeBase64(byte[] bytes) {
            return Base64.encode(bytes);
        }

        private String encodeUTF8(byte[] bytes) throws SaslException {
            try {
                return new String(bytes, "UTF-8");
            } catch (UnsupportedEncodingException e2) {
                throw new SaslException("UTF-8 is not a supported encoding.", e2);
            }
        }

        private byte[] h(byte[] data) throws SaslException {
            try {
                return MessageDigest.getInstance(this.hAlgorithm).digest(data);
            } catch (NoSuchAlgorithmException e2) {
                throw new SaslException(String.format("Algorithm for '%s' could not be found.", this.hAlgorithm), e2);
            }
        }

        private byte[] hi(byte[] password, byte[] salt, int iterations) throws SaslException {
            try {
                SecretKeySpec key = new SecretKeySpec(password, this.hmacAlgorithm);
                Mac mac = Mac.getInstance(this.hmacAlgorithm);
                mac.init(key);
                mac.update(salt);
                mac.update(INT_1);
                byte[] result = mac.doFinal();
                byte[] previous = null;
                for (int i2 = 1; i2 < iterations; ++i2) {
                    mac.update(previous != null ? previous : result);
                    previous = mac.doFinal();
                    this.xorInPlace(result, previous);
                }
                return result;
            } catch (NoSuchAlgorithmException e2) {
                throw new SaslException(String.format("Algorithm for '%s' could not be found.", this.hmacAlgorithm), e2);
            } catch (InvalidKeyException e3) {
                throw new SaslException(String.format("Invalid key for %s", this.hmacAlgorithm), e3);
            }
        }

        private byte[] hmac(byte[] bytes, String key) throws SaslException {
            try {
                Mac mac = Mac.getInstance(this.hmacAlgorithm);
                mac.init(new SecretKeySpec(bytes, this.hmacAlgorithm));
                return mac.doFinal(this.decodeUTF8(key));
            } catch (NoSuchAlgorithmException e2) {
                throw new SaslException(String.format("Algorithm for '%s' could not be found.", this.hmacAlgorithm), e2);
            } catch (InvalidKeyException e3) {
                throw new SaslException("Could not initialize mac.", e3);
            }
        }

        private HashMap<String, String> parseServerResponse(String response) {
            String[] pairs;
            HashMap<String, String> map = new HashMap<String, String>();
            for (String pair : pairs = response.split(",")) {
                String[] parts = pair.split("=", 2);
                map.put(parts[0], parts[1]);
            }
            return map;
        }

        private String getUserName() {
            String userName = this.credential.getCredential().getUserName();
            if (userName == null) {
                throw new IllegalArgumentException("Username can not be null");
            }
            return userName.replace("=", "=3D").replace(",", "=2C");
        }

        private String getAuthenicationHash() {
            String password = this.authenticationHashGenerator.generate(this.credential.getCredential());
            if (this.credential.getAuthenticationMechanism() == AuthenticationMechanism.SCRAM_SHA_256) {
                password = SaslPrep.saslPrepStored(password);
            }
            return password;
        }

        private byte[] xorInPlace(byte[] a2, byte[] b2) {
            for (int i2 = 0; i2 < a2.length; ++i2) {
                int n2 = i2;
                a2[n2] = (byte)(a2[n2] ^ b2[i2]);
            }
            return a2;
        }

        private byte[] xor(byte[] a2, byte[] b2) {
            byte[] result = new byte[a2.length];
            System.arraycopy(a2, 0, result, 0, a2.length);
            return this.xorInPlace(result, b2);
        }
    }
}

