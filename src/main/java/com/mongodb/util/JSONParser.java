/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.mongodb.util;

import com.mongodb.util.JSONCallback;
import com.mongodb.util.JSONParseException;
import org.bson.BSONCallback;

class JSONParser {
    final String s;
    int pos = 0;
    final BSONCallback _callback;

    JSONParser(String s2) {
        this(s2, null);
    }

    JSONParser(String s2, BSONCallback callback) {
        this.s = s2;
        this._callback = callback == null ? new JSONCallback() : callback;
    }

    public Object parse() {
        return this.parse(null);
    }

    protected Object parse(String name) {
        Object value = null;
        char current = this.get();
        switch (current) {
            case 'n': {
                this.read('n');
                this.read('u');
                this.read('l');
                this.read('l');
                value = null;
                break;
            }
            case 'N': {
                this.read('N');
                this.read('a');
                this.read('N');
                value = Double.NaN;
                break;
            }
            case 't': {
                this.read('t');
                this.read('r');
                this.read('u');
                this.read('e');
                value = true;
                break;
            }
            case 'f': {
                this.read('f');
                this.read('a');
                this.read('l');
                this.read('s');
                this.read('e');
                value = false;
                break;
            }
            case '\"': 
            case '\'': {
                value = this.parseString(true);
                break;
            }
            case '+': 
            case '-': 
            case '0': 
            case '1': 
            case '2': 
            case '3': 
            case '4': 
            case '5': 
            case '6': 
            case '7': 
            case '8': 
            case '9': {
                value = this.parseNumber();
                break;
            }
            case '[': {
                value = this.parseArray(name);
                break;
            }
            case '{': {
                value = this.parseObject(name);
                break;
            }
            default: {
                throw new JSONParseException(this.s, this.pos);
            }
        }
        return value;
    }

    public Object parseObject() {
        return this.parseObject(null);
    }

    protected Object parseObject(String name) {
        if (name != null) {
            this._callback.objectStart(name);
        } else {
            this._callback.objectStart();
        }
        this.read('{');
        char current = this.get();
        while (this.get() != '}') {
            String key = this.parseString(false);
            this.read(':');
            Object value = this.parse(key);
            this.doCallback(key, value);
            current = this.get();
            if (current != ',') break;
            this.read(',');
        }
        this.read('}');
        return this._callback.objectDone();
    }

    protected void doCallback(String name, Object value) {
        if (value == null) {
            this._callback.gotNull(name);
        } else if (value instanceof String) {
            this._callback.gotString(name, (String)value);
        } else if (value instanceof Boolean) {
            this._callback.gotBoolean(name, (Boolean)value);
        } else if (value instanceof Integer) {
            this._callback.gotInt(name, (Integer)value);
        } else if (value instanceof Long) {
            this._callback.gotLong(name, (Long)value);
        } else if (value instanceof Double) {
            this._callback.gotDouble(name, (Double)value);
        }
    }

    public void read(char ch) {
        if (!this.check(ch)) {
            throw new JSONParseException(this.s, this.pos);
        }
        ++this.pos;
    }

    public char read() {
        if (this.pos >= this.s.length()) {
            throw new IllegalStateException("string done");
        }
        return this.s.charAt(this.pos++);
    }

    public void readHex() {
        if (this.pos < this.s.length() && (this.s.charAt(this.pos) >= '0' && this.s.charAt(this.pos) <= '9' || this.s.charAt(this.pos) >= 'A' && this.s.charAt(this.pos) <= 'F' || this.s.charAt(this.pos) >= 'a' && this.s.charAt(this.pos) <= 'f')) {
            ++this.pos;
        } else {
            throw new JSONParseException(this.s, this.pos);
        }
    }

    public boolean check(char ch) {
        return this.get() == ch;
    }

    public void skipWS() {
        while (this.pos < this.s.length() && Character.isWhitespace(this.s.charAt(this.pos))) {
            ++this.pos;
        }
    }

    public char get() {
        this.skipWS();
        if (this.pos < this.s.length()) {
            return this.s.charAt(this.pos);
        }
        return '\uffff';
    }

    public String parseString(boolean needQuote) {
        char quot = '\u0000';
        if (this.check('\'')) {
            quot = '\'';
        } else if (this.check('\"')) {
            quot = '\"';
        } else if (needQuote) {
            throw new JSONParseException(this.s, this.pos);
        }
        if (quot > '\u0000') {
            this.read(quot);
        }
        StringBuilder buf = new StringBuilder();
        int start = this.pos;
        block9: while (this.pos < this.s.length()) {
            char current = this.s.charAt(this.pos);
            if (quot <= '\u0000' ? current == ':' || current == ' ' : current == quot) break;
            if (current == '\\') {
                ++this.pos;
                char x2 = this.get();
                char special = '\u0000';
                switch (x2) {
                    case 'u': {
                        buf.append(this.s.substring(start, this.pos - 1));
                        int tempPos = ++this.pos;
                        this.readHex();
                        this.readHex();
                        this.readHex();
                        this.readHex();
                        int codePoint = Integer.parseInt(this.s.substring(tempPos, tempPos + 4), 16);
                        buf.append((char)codePoint);
                        start = this.pos;
                        continue block9;
                    }
                    case 'n': {
                        special = '\n';
                        break;
                    }
                    case 'r': {
                        special = '\r';
                        break;
                    }
                    case 't': {
                        special = '\t';
                        break;
                    }
                    case 'b': {
                        special = '\b';
                        break;
                    }
                    case '\"': {
                        special = '\"';
                        break;
                    }
                    case '\\': {
                        special = '\\';
                        break;
                    }
                }
                buf.append(this.s.substring(start, this.pos - 1));
                if (special != '\u0000') {
                    buf.append(special);
                }
                start = ++this.pos;
                continue;
            }
            ++this.pos;
        }
        buf.append(this.s.substring(start, this.pos));
        if (quot > '\u0000') {
            this.read(quot);
        }
        return buf.toString();
    }

    public Number parseNumber() {
        this.get();
        int start = this.pos;
        boolean isDouble = false;
        if (this.check('-') || this.check('+')) {
            ++this.pos;
        }
        block7: while (this.pos < this.s.length()) {
            switch (this.s.charAt(this.pos)) {
                case '0': 
                case '1': 
                case '2': 
                case '3': 
                case '4': 
                case '5': 
                case '6': 
                case '7': 
                case '8': 
                case '9': {
                    ++this.pos;
                    continue block7;
                }
                case '.': {
                    isDouble = true;
                    this.parseFraction();
                    continue block7;
                }
                case 'E': 
                case 'e': {
                    isDouble = true;
                    this.parseExponent();
                    continue block7;
                }
            }
        }
        try {
            if (isDouble) {
                return Double.valueOf(this.s.substring(start, this.pos));
            }
            Long val = Long.valueOf(this.s.substring(start, this.pos));
            if (val <= Integer.MAX_VALUE && val >= Integer.MIN_VALUE) {
                return val.intValue();
            }
            return val;
        } catch (NumberFormatException e2) {
            throw new JSONParseException(this.s, start, e2);
        }
    }

    public void parseFraction() {
        ++this.pos;
        block4: while (this.pos < this.s.length()) {
            switch (this.s.charAt(this.pos)) {
                case '0': 
                case '1': 
                case '2': 
                case '3': 
                case '4': 
                case '5': 
                case '6': 
                case '7': 
                case '8': 
                case '9': {
                    ++this.pos;
                    continue block4;
                }
                case 'E': 
                case 'e': {
                    this.parseExponent();
                    continue block4;
                }
            }
            break;
        }
    }

    public void parseExponent() {
        ++this.pos;
        if (this.check('-') || this.check('+')) {
            ++this.pos;
        }
        block3: while (this.pos < this.s.length()) {
            switch (this.s.charAt(this.pos)) {
                case '0': 
                case '1': 
                case '2': 
                case '3': 
                case '4': 
                case '5': 
                case '6': 
                case '7': 
                case '8': 
                case '9': {
                    ++this.pos;
                    continue block3;
                }
            }
            break;
        }
    }

    public Object parseArray() {
        return this.parseArray(null);
    }

    protected Object parseArray(String name) {
        if (name != null) {
            this._callback.arrayStart(name);
        } else {
            this._callback.arrayStart();
        }
        this.read('[');
        int i2 = 0;
        char current = this.get();
        while (current != ']') {
            String elemName = String.valueOf(i2++);
            Object elem = this.parse(elemName);
            this.doCallback(elemName, elem);
            current = this.get();
            if (current == ',') {
                this.read(',');
                continue;
            }
            if (current == ']') break;
            throw new JSONParseException(this.s, this.pos);
        }
        this.read(']');
        return this._callback.arrayDone();
    }
}

