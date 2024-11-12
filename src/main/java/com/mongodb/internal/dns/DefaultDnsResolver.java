/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.internal.dns;

import com.mongodb.MongoClientException;
import com.mongodb.MongoConfigurationException;
import com.mongodb.internal.dns.DnsResolver;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;

public final class DefaultDnsResolver
implements DnsResolver {
    @Override
    public List<String> resolveHostFromSrvRecords(String srvHost) {
        String srvHostDomain = srvHost.substring(srvHost.indexOf(46) + 1);
        List<String> srvHostDomainParts = Arrays.asList(srvHostDomain.split("\\."));
        ArrayList<String> hosts = new ArrayList<String>();
        InitialDirContext dirContext = DefaultDnsResolver.createDnsDirContext();
        try {
            Attributes attributes = dirContext.getAttributes("_mongodb._tcp." + srvHost, new String[]{"SRV"});
            Attribute attribute = attributes.get("SRV");
            if (attribute == null) {
                throw new MongoConfigurationException("No SRV records available for host " + srvHost);
            }
            NamingEnumeration<?> srvRecordEnumeration = attribute.getAll();
            while (srvRecordEnumeration.hasMore()) {
                String srvRecord = (String)srvRecordEnumeration.next();
                String[] split = srvRecord.split(" ");
                String resolvedHost = split[3].endsWith(".") ? split[3].substring(0, split[3].length() - 1) : split[3];
                String resolvedHostDomain = resolvedHost.substring(resolvedHost.indexOf(46) + 1);
                if (!DefaultDnsResolver.sameParentDomain(srvHostDomainParts, resolvedHostDomain)) {
                    throw new MongoConfigurationException(String.format("The SRV host name '%s'resolved to a host '%s 'that is not in a sub-domain of the SRV host.", srvHost, resolvedHost));
                }
                hosts.add(resolvedHost + ":" + split[2]);
            }
            if (hosts.isEmpty()) {
                throw new MongoConfigurationException("Unable to find any SRV records for host " + srvHost);
            }
        } catch (NamingException e2) {
            throw new MongoConfigurationException("Unable to look up SRV record for host " + srvHost, e2);
        } finally {
            try {
                dirContext.close();
            } catch (NamingException namingException) {}
        }
        return hosts;
    }

    private static boolean sameParentDomain(List<String> srvHostDomainParts, String resolvedHostDomain) {
        List<String> resolvedHostDomainParts = Arrays.asList(resolvedHostDomain.split("\\."));
        if (srvHostDomainParts.size() > resolvedHostDomainParts.size()) {
            return false;
        }
        return resolvedHostDomainParts.subList(resolvedHostDomainParts.size() - srvHostDomainParts.size(), resolvedHostDomainParts.size()).equals(srvHostDomainParts);
    }

    @Override
    public String resolveAdditionalQueryParametersFromTxtRecords(String host) {
        String additionalQueryParameters = "";
        InitialDirContext dirContext = DefaultDnsResolver.createDnsDirContext();
        try {
            NamingEnumeration<?> txtRecordEnumeration;
            Attributes attributes = dirContext.getAttributes(host, new String[]{"TXT"});
            Attribute attribute = attributes.get("TXT");
            if (attribute != null && (txtRecordEnumeration = attribute.getAll()).hasMore()) {
                additionalQueryParameters = ((String)txtRecordEnumeration.next()).replaceAll("\\s", "");
                if (txtRecordEnumeration.hasMore()) {
                    throw new MongoConfigurationException(String.format("Multiple TXT records found for host '%s'.  Only one is permitted", host));
                }
            }
        } catch (NamingException e2) {
            throw new MongoConfigurationException("Unable to look up TXT record for host " + host, e2);
        } finally {
            try {
                dirContext.close();
            } catch (NamingException namingException) {}
        }
        return additionalQueryParameters;
    }

    private static InitialDirContext createDnsDirContext() {
        Hashtable<String, String> envProps = new Hashtable<String, String>();
        envProps.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
        try {
            return new InitialDirContext(envProps);
        } catch (NamingException e2) {
            throw new MongoClientException("Unable to support mongodb+srv// style connections as the 'com.sun.jndi.dns.DnsContextFactory' class is not available in this JRE. A JNDI context is required for resolving SRV records.", e2);
        }
    }
}

