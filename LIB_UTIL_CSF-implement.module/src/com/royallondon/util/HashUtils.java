package com.royallondon.util;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HashUtils {

	static final Logger LOG = LoggerFactory.getLogger(HashUtils.class);

	private final static String SEPARATOR = "\t";
	
	private class HashEntry {
		final String id;
		final String value1;
		final String value2;
		final String value3;
		final String value4;
		final String value5;

		public HashEntry(String id, String value1, String value2, String value3, String value4, String value5) {
			super();
			this.id = id;
			this.value1 = value1;
			this.value2 = value2;
			this.value3 = value3;
			this.value4 = value4;
			this.value5 = value5;
		}

		public String responseFormat() {
			return this.id + SEPARATOR + this.value1 + SEPARATOR + this.value2 + SEPARATOR + this.value3 + SEPARATOR + this.value4 + SEPARATOR + this.value5;
		}
	}

	
	private final static ConcurrentHashMap<String, ConcurrentHashMap<String, HashEntry>> MAPS = new ConcurrentHashMap<>();
	
	public static void main(String[] args) {
//		HashUtils test = new HashUtils();
//		test.setEntry("map", "1", "v1", "v2", "v3", "v4", "v5");
//		test.setEntry("map", "2", "v1", "v2", "v3", "v4", "v5");
//		test.setEntry("map", "3", "v1", "v2", "v3", "v4", "v5");
//		test.setEntry("map", "4", "v2", "v2", "v3", "v4", "v5");
//		test.setEntry("map", "5", "v3", "v2", "v3", "v4", "v5");
//		test.setEntry("map", "6", "xz3", "v2", "v3", "v4", "v5");
//		
//		String[] entries = test.findEntries("map", "v1", "=", "", "", "", "", "", "", "", "");
//		String[] keys = test.findKeys("map", "v1", "=", "", "", "", "", "", "", "", "");
//		String[] single = test.findEntries("map", "v2", "=", "", "", "", "", "", "", "", "");
//		String[] first = test.findEntries("map", "v", "[", "", "", "", "", "", "", "", "");
//		String[] ends = test.findEntries("map", "1", "]", "", "", "", "", "", "", "", "");
//		String[] contains = test.findEntries("map", "1", "*", "", "", "", "", "", "", "", "");
//		String[] contains2 = test.findEntries("map", "v", "*", "", "", "", "", "", "", "", "");
//		String[] contains3 = test.findEntries("map", "z", "*", "", "", "", "", "", "", "", "");
//		
//		System.exit(0);
	}	

	
	public HashUtils() {
		super();
	}

	public void done() {
//		System.out.println("Done");
	}

	
	public final void setEntry(String mapname, String key, String value1, String value2, String value3, String value4, String value5)
	{
		ConcurrentHashMap<String, HashEntry>map = MAPS.get(mapname);
		if (map == null) {
			map = new ConcurrentHashMap<String, HashEntry>();
			MAPS.put(mapname, map);
		}

		HashEntry entry = new HashEntry(key, value1, value2, value3, value4, value5);
		map.put(key, entry);
		
		if (LOG.isDebugEnabled())
			LOG.debug("Added hash entry to '" + mapname + "' - '" + entry.responseFormat().replace(SEPARATOR, "/") + "'");
	}

	public final void setEntries(String mapname, String key[], String value1[], String value2[], String value3[], String value4[], String value5[])
	{
		ConcurrentHashMap<String, HashEntry>map = new ConcurrentHashMap<String, HashEntry>();

		LOG.debug("Clearing hash '" + mapname + "'");

		for (int i = 0; i < key.length; ++i) {
			HashEntry e = new HashEntry(key[i], value1[i],
										value2.length > i ? value2[i] : "",
										value3.length > i ? value3[i] : "",
										value4.length > i ? value4[i] : "",
										value5.length > i ? value5[i] : "");
			map.put(key[i], e);
			
			if (LOG.isDebugEnabled())
				LOG.debug("Added hash entry to '" + mapname + "' - '" + e.responseFormat().replace(SEPARATOR, "/") + "'");
		}

		MAPS.put(mapname, map);

		LOG.debug("Set hash '" + mapname + "' - entries = " + map.size());
	}

	public final void removeEntry(String mapname, String key)
	{
		ConcurrentHashMap<String, HashEntry>map = MAPS.get(mapname);
		if (map != null) {
			HashEntry entry = map.remove(key);

			if (LOG.isDebugEnabled()) {
				if (entry == null)
					LOG.debug("Removed hash entry from '" + mapname + "' - no entry found for key '" + key + "'");
				else
					LOG.debug("Removed hash entry from '" + mapname + "' - '" + entry.responseFormat().replace(SEPARATOR, "/") + "'");
			}
		}
	}

	public final void clear(String mapname)
	{
		ConcurrentHashMap<String, HashEntry>map = MAPS.get(mapname);
		if (map != null) {
			map.clear();

			LOG.debug("Cleared hash '" + mapname + "'");
		}
	}

	public final String getEntry(String mapname, String key)
	{
		ConcurrentHashMap<String, HashEntry>map = MAPS.get(mapname);
		if (map != null) {
			HashEntry entry = map.get(key);
			if (entry != null) {
				return entry.responseFormat();
			}
		}
		
		return "";
	}

	public final String getEntryValue(String mapname, String key, int value)
	{
		ConcurrentHashMap<String, HashEntry>map = MAPS.get(mapname);
		if (map != null) {
			HashEntry entry = map.get(key);
			if (entry != null) {
				switch (value) {
					case 1:
						return entry.value1;
					case 2:
						return entry.value2;
					case 3:
						return entry.value3;
					case 4:
						return entry.value4;
					case 5:
						return entry.value5;
					default:
						return "";
				}
			}
		}
		
		return "";
	}

	public final String[] getEntries(String mapname)
	{
		ConcurrentHashMap<String, HashEntry>map = MAPS.get(mapname);
		if (map == null)
			return new String[0];

		ArrayList<String> reply = new ArrayList<>(map.size());
		for (HashEntry entry : map.values()) {
			reply.add(entry.responseFormat());
		}
		
		return reply.toArray(new String[0]);
	}

	public final String[] getKeys(String mapname)
	{
		ConcurrentHashMap<String, HashEntry>map = MAPS.get(mapname);
		if (map == null)
			return new String[0];

		ArrayList<String> reply = new ArrayList<>(map.size());
		for (HashEntry entry : map.values()) {
			reply.add(entry.id);
		}
		
		return reply.toArray(new String[0]);
	}

	public final void rename(String mapname, String toname)
	{
		ConcurrentHashMap<String, HashEntry>map = MAPS.remove(mapname);
		if (map != null) {
			MAPS.put(toname, map);
		}
	}


	public final String[] findEntries(String mapname, String value1, String comp1, String value2, String comp2, String value3, String comp3, String value4, String comp4, String value5, String comp5)
	{
		ConcurrentHashMap<String, HashEntry>map = MAPS.get(mapname);
		if (map == null)
			return new String[0];

		ArrayList<String> reply = new ArrayList<>(map.size());
		for (HashEntry entry : map.values()) {
			if (compare(entry.value1, value1, comp1))
				reply.add(entry.responseFormat());
			else if (compare(entry.value2, value2, comp2))
				reply.add(entry.responseFormat());
			else if (compare(entry.value3, value3, comp3))
				reply.add(entry.responseFormat());
			else if (compare(entry.value4, value4, comp4))
				reply.add(entry.responseFormat());
			else if (compare(entry.value5, value5, comp5))
				reply.add(entry.responseFormat());
		}
		
		return reply.toArray(new String[0]);
	}

	
	public final String[] findKeys(String mapname, String value1, String comp1, String value2, String comp2, String value3, String comp3, String value4, String comp4, String value5, String comp5)
	{
		ConcurrentHashMap<String, HashEntry>map = MAPS.get(mapname);
		if (map == null)
			return new String[0];

		ArrayList<String> reply = new ArrayList<>(map.size());
		for (HashEntry entry : map.values()) {
			if (compare(entry.value1, value1, comp1))
				reply.add(entry.id);
			else if (compare(entry.value2, value2, comp2))
				reply.add(entry.id);
			else if (compare(entry.value3, value3, comp3))
				reply.add(entry.id);
			else if (compare(entry.value4, value4, comp4))
				reply.add(entry.id);
			else if (compare(entry.value5, value5, comp5))
				reply.add(entry.id);
		}
		
		return reply.toArray(new String[0]);
	}
	
	
	private boolean compare(String value, String test, String op) {
		if ((value == null) || (test == null) || (op == null) || (value.length() == 0) || (test.length() == 0))
			return false;
		else if (op.equals("=") && value.regionMatches(true, 0, test, 0, test.length()))
			return true;
		else if (op.equals("[") && value.regionMatches(true, 0, test, 0, test.length()))
			return true;
		else if (op.equals("]") && (value.length() >= test.length()) && value.regionMatches(true, value.length() - test.length(), test, 0, test.length()))
			return true;
		else if (op.equals("*")) {
			final int length = test.length();
		    final char firstLo = Character.toLowerCase(test.charAt(0));
		    final char firstUp = Character.toUpperCase(test.charAt(0));

		    for (int i = value.length() - length; i >= 0; i--) {
		        // Quick check before calling the more expensive regionMatches() method:
		        char ch = value.charAt(i);
		        if (ch != firstLo && ch != firstUp)
		            continue;

		        if (value.regionMatches(true, i, test, 0, length))
		            return true;
		    }

		    return false;
		}
		else
			return false;
	}
}
