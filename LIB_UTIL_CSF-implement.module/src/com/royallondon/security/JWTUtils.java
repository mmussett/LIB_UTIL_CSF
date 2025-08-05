package com.royallondon.security;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.MissingClaimException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SigningKeyResolver;
import io.jsonwebtoken.UnsupportedJwtException;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JWTUtils implements SigningKeyResolver {

	static final Logger LOG = LoggerFactory.getLogger(JWTUtils.class);

	public static void main(String[] args) throws Exception {
		JWTUtils.setRSAPublicKey("a3rMUgMFv9tPclLa6yF3zAkfquE", "qnTksBdxOiOlsmRNd-mMS2M3o1IDpK4uAr0T4_YqO3zYHAGAWTwsq4ms-NWynqY5HaB4EThNxuq2GWC5JKpO1YirOrwS97B5x9LJyHXPsdJcSikEI9BxOkl6WLQ0UzPxHdYTLpR4_O-0ILAlXw8NU4-jB4AP8Sn9YGYJ5w0fLw5YmWioXeWvocz1wHrZdJPxS8XnqHXwMUozVzQj-x6daOv5FmrHU1r9_bbp0a1GLv4BbTtSh4kMyz1hXylho0EvPg5p9YIKStbNAW9eNWvv5R8HN7PPei21AsUqxekK0oW9jnEdHewckToX7x5zULWKwwZIksll0XnVczVgy7fCFw", "AQAB", 100000000);
//		JWTUtils.setRSAPublicKey("MIIDBTCCAfGgAwIBAgIQNQb+T2ncIrNA6cKvUA1GWTAJBgUrDgMCHQUAMBIxEDAOBgNVBAMTB0RldlJvb3QwHhcNMTAwMTIwMjIwMDAwWhcNMjAwMTIwMjIwMDAwWjAVMRMwEQYDVQQDEwppZHNydjN0ZXN0MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqnTksBdxOiOlsmRNd+mMS2M3o1IDpK4uAr0T4/YqO3zYHAGAWTwsq4ms+NWynqY5HaB4EThNxuq2GWC5JKpO1YirOrwS97B5x9LJyHXPsdJcSikEI9BxOkl6WLQ0UzPxHdYTLpR4/O+0ILAlXw8NU4+jB4AP8Sn9YGYJ5w0fLw5YmWioXeWvocz1wHrZdJPxS8XnqHXwMUozVzQj+x6daOv5FmrHU1r9/bbp0a1GLv4BbTtSh4kMyz1hXylho0EvPg5p9YIKStbNAW9eNWvv5R8HN7PPei21AsUqxekK0oW9jnEdHewckToX7x5zULWKwwZIksll0XnVczVgy7fCFwIDAQABo1wwWjATBgNVHSUEDDAKBggrBgEFBQcDATBDBgNVHQEEPDA6gBDSFgDaV+Q2d2191r6A38tBoRQwEjEQMA4GA1UEAxMHRGV2Um9vdIIQLFk7exPNg41NRNaeNu0I9jAJBgUrDgMCHQUAA4IBAQBUnMSZxY5xosMEW6Mz4WEAjNoNv2QvqNmk23RMZGMgr516ROeWS5D3RlTNyU8FkstNCC4maDM3E0Bi4bbzW3AwrpbluqtcyMN3Pivqdxx+zKWKiORJqqLIvN8CT1fVPxxXb/e9GOdaR8eXSmB0PgNUhM4IjgNkwBbvWC9F/lzvwjlQgciR7d4GfXPYsE1vf8tmdQaY8/PtdAkExmbrb9MihdggSoGXlELrPA91Yce+fiRcKY3rQlNWVd4DOoJ/cPXsXwry8pWjNCo5JD8Q+RQ5yZEy7YPoifwemLhTdsBz3hlZr28oCGJ3kbnpW0xGvQb3VHSTVVbeei0CfXoW6iz1", 100000000);
		String[] x = JWTUtils.validateJWT("eyJhbGciOiJSUzI1NiIsImtpZCI6IjI3QzlCMjk2MDhCMzc5NjMwOThDNjI0NUJCM0ZEMTc3MzlCQjQ0RTkiLCJ0eXAiOiJKV1QiLCJ4NXQiOiJKOG15bGdpemVXTUpqR0pGdXpfUmR6bTdST2sifQ.eyJuYmYiOjE1NDQxMDkyNzcsImV4cCI6MTU0NDEwOTU3NywiaXNzIjoiaHR0cHM6Ly9jaXQtd3d3LnRlc3Ryb3lhbGxvbmRvbi5jb20vc2VjdXJlL2lkZW50aXR5L2V4dGVybmFsIiwiYXVkIjoiU2hlbGwiLCJub25jZSI6IjQxM2M4MTRkOTdkNTRmZDE4YjdiZjFhMmEwMjk2OGJhIiwiaWF0IjoxNTQ0MTA5Mjc3LCJhdF9oYXNoIjoiQm03dXd6dVB1cDJsd3ZtX2Etdmo5dyIsInNpZCI6IjY2ODVjNGViNWJjYWNjMTFmOGViNWNmY2MzNTI5YjY1Iiwic3ViIjoiNzIxZGFlMDYtZjc4OS00MzQwLWFhODAtNDNiZmQ1OWU1ZDdmIiwiYXV0aF90aW1lIjoxNTQ0MTA5Mjc3LCJpZHAiOiJsb2NhbCIsInByZWZlcnJlZF91c2VybmFtZSI6Imhhbm5haEByb3lhbGxvbmRvbi5jb20iLCJnaXZlbl9uYW1lIjoiSGFubmFoIiwiZmFtaWx5X25hbWUiOiJLZWlsbGVyIiwibmFtZSI6Ikhhbm5haCwgS2VpbGxlciIsInVzZXJfcm9sZSI6ImV4dGVybmFsIiwiYWR2aXNlcl9pZCI6IkFEVjAwMTQzMzQzIiwiYXBpX2tleSI6ImZranhiZXZhNmVieDdkZ211YWZzdjR1YyIsImFtciI6WyJwd2QiXX0.lby-HEwHUox52j2xuVZcBu7Xpy1amgUcc_9IzAYWkpnv7QqvxI1gUZ9LYab4yT8nP4ri2Jp6t4-_CygI7r0CCI_ChFvdoMNaQXI3sYgvMEKdU8vdUUxf1G6Lf39Q-OR1cKVUS-XG6paGreAIBVUMfNMpbZKZsYAz0MI7Qmld-m0w8yJEo98M1gr04WsFYLdGn8K5xBiHt7SawyendPgSvL8Uvx1FZ1xpfMJ3kRA_JJSK9kauM-1FZiuoCTBeLrtjVTnhe0hhv7QdzOeVJDyzSFx7y6Thr3CAP10jTu7ODBzzz8-QIIJjeczlM-Z4Mlxe2-9G9-8bdvSHByp4aI-HFA");
		for (String y : x) {
			System.out.println(y);
		}
		
		JWTUtils.readCertificates("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");
		System.exit(0);
	}
	
	private static JwtParser _jwtParser;
	private static ConcurrentHashMap<String, Key> _signingKeys = new ConcurrentHashMap<>();
	
	public JWTUtils() {
		super();
	}

	public static void readCertificates(String certs) throws Exception {
		
		ByteArrayInputStream bis = new ByteArrayInputStream(Base64.getDecoder().decode(certs));
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		
		 while (bis.available() > 0) {
		    Certificate cert = cf.generateCertificate(bis);
		    System.out.println(cert.toString());
		 }
	}

	public static String createJWT(String storePath, String storePassword, String keyAlias, String keyPassword, int expireSecs) throws Exception {
		FileInputStream fis = new FileInputStream(storePath);
		KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		keystore.load(fis,  storePassword.toCharArray());
		
		Calendar expiration = Calendar.getInstance();
		expiration.add(Calendar.SECOND, expireSecs);
		
		Key key = keystore.getKey(keyAlias,  keyPassword.toCharArray());
		String jws = Jwts.builder().setNotBefore(new Date()).setExpiration(expiration.getTime()).signWith(SignatureAlgorithm.RS256, key).compact();
		
		return jws;
	}

	public static String setRSAPublicKey(String kid, String modStr, String expStr, long clockSkewSeconds) {
		try {
			BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode(modStr));
			BigInteger exponent = new BigInteger(1, Base64.getUrlDecoder().decode(expStr));
			
			Key publicKey = KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(modulus, exponent));
			_signingKeys.put(kid, publicKey);
//			_jwtParser = Jwts.parser().setAllowedClockSkewSeconds(clockSkewSeconds).setSigningKey(publicKey);
			_jwtParser = Jwts.parser().setAllowedClockSkewSeconds(clockSkewSeconds).setSigningKeyResolver(new JWTUtils());
		
			return "";
		}
		catch (Exception ex) {
			_jwtParser = null;
			return ex.getMessage();
		}
	}

	public static String[] getRSAKeyIds() {
		ArrayList<String> keys = new ArrayList<String>(_signingKeys.size());
		for (Entry<String, Key>entry: _signingKeys.entrySet()) {
			keys.add(entry.getKey());
		}
		
		return keys.toArray(new String[0]);
	}

	
	public static String removeRSAPublicKeys(String[] kids) {
		StringBuilder result = new StringBuilder();
		for (String kid : kids) {
			if (_signingKeys.remove(kid) != null) {
				if (result.length() > 0)
					result.append(",");
				result.append(kid);
			}
		}
		
		return result.toString();
	}
	
	public static boolean isRSAPublicKeyInitialised() {
		return _jwtParser != null;
	}

	
	public static String[] validateJWT(String jwtString) {

		if (!isRSAPublicKeyInitialised()) {
			return new String[] { "RSA key is not initialised" };
		}

		try {
			Jws<Claims> jws = _jwtParser.parseClaimsJws(jwtString);
			Claims claims = jws.getBody();
			if (claims.getNotBefore() == null) {
				throw new MissingClaimException(jws.getHeader(), claims, "No nbf claim");	
			}
			else if (claims.getExpiration() == null) {
				throw new MissingClaimException(jws.getHeader(), claims, "No exp claim");	
			}
			else if (claims.getExpiration() == null) {
				throw new MissingClaimException(jws.getHeader(), claims, "No subject claim");	
			}
	
			String claimValues[] = new String[claims.size()];
			int index = 0;
			for (Entry<String,Object> claim : claims.entrySet()) {
				claimValues[index++] = claim.getKey() + "=" + claim.getValue().toString();
			}
		
			return claimValues;
		}
		catch (Exception ex) {

			String input = ex.getMessage();
			byte[] ascii = new byte[input.length()];
			for (int i = 0; i < input.length(); i++) {
			    char ch = input.charAt(i);
			    ascii[i] = (ch >= 0x20) && (ch <= 0x7F) ? (byte) ch : (byte) '?';
			}
					
			String reply = new String(ascii);
			if (reply.startsWith("A signing key must be specified"))
				return new String[] { "The signing key id for this JWT is not known by the IDS server" };
			else
				return new String[] { new String(ascii) };
		}
	}

	@Override
	public Key resolveSigningKey(@SuppressWarnings("rawtypes") JwsHeader header, Claims claims) {
		String kid = header.getKeyId();
		if (kid != null)
			return _signingKeys.get(kid);
		else
			return null;
	}

	@Override
	public Key resolveSigningKey(@SuppressWarnings("rawtypes") JwsHeader header, String plaintext) {
//        throw new UnsupportedJwtException("The specified SigningKeyResolver implementation does not support " +
//                "plaintext JWS signing key resolution.  Consider overriding either the " +
//                "resolveSigningKey(JwsHeader, String) method or, for HMAC algorithms, the " +
//                "resolveSigningKeyBytes(JwsHeader, String) method.");
        throw new UnsupportedJwtException("Plaintext JWS signing key resolution is not supported");
	}
	
	public static String[] ProcessJWTTokenAllowExpired(String jwtString) {

		if (!isRSAPublicKeyInitialised()) {
			return new String[] { "RSA key is not initialised" };
		}

		try {
			Jws<Claims> jws = _jwtParser.parseClaimsJws(jwtString);
			Claims claims = jws.getBody();
			if (claims.getNotBefore() == null) {
				throw new MissingClaimException(jws.getHeader(), claims, "No nbf claim");	
			}
			else if (claims.getExpiration() == null) {
				throw new MissingClaimException(jws.getHeader(), claims, "No exp claim");	
			}
			else if (claims.getExpiration() == null) {
				throw new MissingClaimException(jws.getHeader(), claims, "No subject claim");	
			}
	
			String claimValues[] = new String[claims.size()];
			int index = 0;
			for (Entry<String,Object> claim : claims.entrySet()) {
				claimValues[index++] = claim.getKey() + "=" + claim.getValue().toString();
			}
		
			return claimValues;
		}
		catch(ExpiredJwtException e)
		{
			Claims claims=e.getClaims();
			String claimValues[] = new String[claims.size()];
			int index = 0;
			for (Entry<String,Object> claim : claims.entrySet()) {
				claimValues[index++] = claim.getKey() + "=" + claim.getValue().toString();
			}		

			return claimValues;	
		}
		catch (Exception ex) {

			String input = ex.getMessage();
			byte[] ascii = new byte[input.length()];
			for (int i = 0; i < input.length(); i++) {
			    char ch = input.charAt(i);
			    ascii[i] = (ch >= 0x20) && (ch <= 0x7F) ? (byte) ch : (byte) '?';
			}
					
			String reply = new String(ascii);
			if (reply.startsWith("A signing key must be specified"))
				return new String[] { "The signing key id for this JWT is not known by the IDS server" };
			else
				return new String[] { new String(ascii) };
		}
	}

	
	
}
