package cn.lokn.kncache.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Stream;

/**
 * @description: cache entries.
 * @author: lokn
 * @date: 2024/08/13 23:45
 */
public class KNCache {

    Map<String, CacheEntry<?>> map = new HashMap<>();

    //================== 1.String ====================
    public String get(String key) {
        CacheEntry<String> entry = (CacheEntry<String>) map.get(key);
        return entry.getValue();
    }

    public void set(String key, String value) {
        this.map.put(key, new CacheEntry<>(value));
    }

    public int del(String... keys) {
        if (keys == null) return 0;
        return (int) Arrays.stream(keys).map(map::remove).filter(Objects::nonNull).count();
    }

    public int exists(String... keys) {
        if (keys == null) return 0;
        return (int) Arrays.stream(keys).map(map::containsKey).filter(x -> x).count();
    }

    public String[] mget(String... keys) {
        if (keys == null) return new String[0];
        return Arrays.stream(keys).map(this::get).toArray(String[]::new);
    }

    public void mset(String[] keys, String[] values) {
        if (keys == null || keys.length == 0) return;
        for (int i = 0; i < keys.length; i++) {
            set(keys[i], values[i]);
        }
    }

    public int incr(String key) {
        String str = get(key);
        int val = 0;
        try {
            if (str != null) {
                val = Integer.parseInt(str);
            }
            val++;
            set(key, String.valueOf(val));
        } catch (NumberFormatException e) {
            throw e;
        }
        return val;
    }

    public int decr(String key) {
        String str = get(key);
        int val = 0;
        try {
            if (str != null) {
                val = Integer.parseInt(str);
            }
            val--;
            set(key, String.valueOf(val));
        } catch (NumberFormatException e) {
            throw e;
        }
        return val;
    }

    public Integer strlen(String key) {
        return get(key) == null ? 0 : get(key).length();
    }

    //================== 1.String end  ====================

    //================== 2.list  ====================
    public Integer lpush(String key, String... vals) {
        CacheEntry<LinkedList<String>> entry = (CacheEntry<LinkedList<String>>) map.get(key);
        if (entry == null) {
            entry = new CacheEntry<>(new LinkedList<String>());
            this.map.put(key, entry);
        }
        LinkedList<String> exist = entry.getValue();
        Arrays.stream(vals).forEach(exist::addFirst);
        return vals.length;
    }

    public String[] lpop(String key, int count) {
        CacheEntry<LinkedList<String>> entry = (CacheEntry<LinkedList<String>>) map.get(key);
        if (entry == null) return null;
        LinkedList<String> value = entry.getValue();
        if (value == null) return null;

        int len = value.size();
        int index = Math.min(count, len);
        String[] ret = new String[index];
        for (int i = 0; i < index; i++) {
            ret[i] = value.removeFirst();
        }
        return ret;
    }

    public Integer rpush(String key, String... vals) {
        CacheEntry<LinkedList<String>> entry = (CacheEntry<LinkedList<String>>) map.get(key);
        if (entry == null) {
            entry = new CacheEntry<>(new LinkedList<String>());
            this.map.put(key, entry);
        }
        LinkedList<String> exist = entry.getValue();
        Arrays.stream(vals).forEach(exist::addLast);
        return vals.length;
    }

    public String[] rpop(String key, int count) {
        CacheEntry<LinkedList<String>> entry = (CacheEntry<LinkedList<String>>) map.get(key);
        if (entry == null) return null;
        LinkedList<String> value = entry.getValue();
        if (value == null) return null;

        int len = value.size();
        int index = Math.min(count, len);
        String[] ret = new String[index];
        for (int i = 0; i < index; i++) {
            ret[i] = value.removeLast();
        }
        return ret;
    }

    public int llen(String key) {
        CacheEntry<LinkedList<String>> entry = (CacheEntry<LinkedList<String>>) map.get(key);
        if (entry == null) return 0;
        LinkedList<String> value = entry.getValue();
        if (value == null) return 0;
        return value.size();
    }

    public String lindex(String key, int index) {
        CacheEntry<LinkedList<String>> entry = (CacheEntry<LinkedList<String>>) map.get(key);
        if (entry == null) return null;
        LinkedList<String> value = entry.getValue();
        if (value == null) return null;
        if (index >= value.size()) return null;
        return value.get(index);
    }

    public String[] lrange(String key, int start, int end) {
        CacheEntry<LinkedList<String>> entry = (CacheEntry<LinkedList<String>>) map.get(key);
        if (entry == null) return null;
        LinkedList<String> value = entry.getValue();
        if (value == null) return null;

        int size = value.size();
        if (start >= size) return null;
        if (end >= size) end = size - 1;
        int len = Math.min(size, end - start + 1);
        String[] ret = new String[len];
        for (int i = 0; i < len; i++) {
            ret[i] = value.get(start + i);
        }
        return ret;
    }

    //================== 2.list end ====================


    //================== 3.set  ====================
    public Integer sadd(String key, String[] vals) {
        CacheEntry<LinkedHashSet<String>> entry = (CacheEntry<LinkedHashSet<String>>) map.get(key);
        if (entry == null) {
            entry = new CacheEntry<>(new LinkedHashSet<String>());
            map.put(key, entry);
        }
        LinkedHashSet<String> exist = entry.getValue();
        exist.addAll(Arrays.asList(vals));
        return vals.length;
    }

    public String[] smembers(String key) {
        CacheEntry<LinkedHashSet<String>> entry = (CacheEntry<LinkedHashSet<String>>) map.get(key);
        if (entry == null) return null;
        LinkedHashSet<String> exist = entry.getValue();
        return exist.toArray(String[]::new);
    }

    public Integer scard(String key) {
        CacheEntry<LinkedHashSet<String>> entry = (CacheEntry<LinkedHashSet<String>>) map.get(key);
        if (entry == null) return null;
        LinkedHashSet<String> exist = entry.getValue();
        return exist.size();
    }

    public Integer sismember(String key, String val) {
        CacheEntry<LinkedHashSet<String>> entry = (CacheEntry<LinkedHashSet<String>>) map.get(key);
        if (entry == null) return 0;
        LinkedHashSet<String> exist = entry.getValue();
        return exist.contains(val) ? 1 : 0;
    }

    public Integer srem(String key, String[] vals) {
        CacheEntry<LinkedHashSet<String>> entry = (CacheEntry<LinkedHashSet<String>>) map.get(key);
        if (entry == null) return 0;
        LinkedHashSet<String> exist = entry.getValue();
        return vals == null ? 0 : (int) Arrays.stream(vals)
                .map(exist::remove).filter(x -> x).count();
    }

    Random random = new Random();
    public String[] spop(String key, int count) {
        CacheEntry<LinkedHashSet<String>> entry = (CacheEntry<LinkedHashSet<String>>) map.get(key);
        if (entry == null) return null;
        LinkedHashSet<String> exist = entry.getValue();
        if (exist == null) return null;
        int len = Math.min(count, exist.size());
        String[] ret = new String[count];
        int index = 0;
        while (index < len) {
            String[] array = exist.toArray(String[]::new);
            String obj = array[random.nextInt(exist.size())];
            exist.remove(obj);
            ret[index++] = obj;
        }
        return ret;
    }

    //================== 3.set end ====================

    //================== 4.hash  ====================

    public Integer hset(String key, String[] hkeys, String[] hvals) {
        if (hkeys == null || hkeys.length == 0) return 0;
        if (hvals == null || hkeys.length == 0) return 0;

        CacheEntry<LinkedHashMap<String, String>> entry = (CacheEntry<LinkedHashMap<String, String>>) map.get(key);
        if (entry == null) {
            entry = new CacheEntry<>(new LinkedHashMap<>());
            this.map.put(key, entry);
        }
        LinkedHashMap<String, String> exist = entry.getValue();
        for (int i = 0; i < hkeys.length; i++) {
            exist.put(hkeys[i], hvals[i]);
        }
        return hkeys.length;
    }

    public String hget(String key, String hkey) {
        CacheEntry<LinkedHashMap<String, String>> entry = (CacheEntry<LinkedHashMap<String, String>>) map.get(key);
        if (entry == null) return null;
        LinkedHashMap<String, String> exist = entry.getValue();
        if (exist == null) return null;
        return exist.get(hkey);
    }

    public String[] hgetall(String key) {
        CacheEntry<LinkedHashMap<String, String>> entry = (CacheEntry<LinkedHashMap<String, String>>)map.get(key);
        if (entry == null) return null;
        LinkedHashMap<String, String> exist = entry.getValue();
        return exist.entrySet().stream()
                .flatMap(e -> Stream.of(e.getKey(), e.getValue()))
                .toArray(String[]::new);
    }

    public String[] hmget(String key, String[] hkeys) {
        CacheEntry<LinkedHashMap<String, String>> entry = (CacheEntry<LinkedHashMap<String, String>>)map.get(key);
        if (entry == null) return null;
        LinkedHashMap<String, String> exist = entry.getValue();

        return hkeys != null ? new String[0] : Arrays.stream(hkeys)
                .map(exist::get).toArray(String[]::new);
    }

    public Integer hlen(String key) {
        CacheEntry<LinkedHashMap<String, String>> entry = (CacheEntry<LinkedHashMap<String, String>>)map.get(key);
        if (entry == null) return 0;
        LinkedHashMap<String, String> exist = entry.getValue();
        return exist.size();
    }

    public Integer hexists(String key, String hkey) {
        CacheEntry<LinkedHashMap<String, String>> entry = (CacheEntry<LinkedHashMap<String, String>>)map.get(key);
        if (entry == null) return 0;
        LinkedHashMap<String, String> exist = entry.getValue();
        return exist.containsKey(hkey) ? 1 : 0;
    }

    public Integer hdel(String key, String[] hkeys) {
        CacheEntry<LinkedHashMap<String, String>> entry = (CacheEntry<LinkedHashMap<String, String>>)map.get(key);
        if (entry == null) return 0;
        LinkedHashMap<String, String> exist = entry.getValue();
        return hkeys == null ? 0 : (int) Arrays.stream(hkeys)
                .map(exist::remove)
                .filter(Objects::nonNull)
                .count();
    }

    //================== 4.hash end ====================

    //================== 5.zset  ====================
    public Integer zadd(String key, String[] vals, double[] scores) {
        CacheEntry<LinkedHashSet<ZsetEntry>> entry = (CacheEntry<LinkedHashSet<ZsetEntry>>) map.get(key);
        if (entry == null) {
            entry = new CacheEntry<>(new LinkedHashSet<ZsetEntry>());
            map.put(key, entry);
        }
        LinkedHashSet<ZsetEntry> exist = entry.getValue();
        for (int i = 0; i < vals.length; i++) {
            exist.add(new ZsetEntry(vals[i], scores[i]));
        }
        return vals.length;
    }

    public Integer zcard(String key) {
        CacheEntry<LinkedHashSet<ZsetEntry>> entry = (CacheEntry<LinkedHashSet<ZsetEntry>>) map.get(key);
        if (entry == null) return 0;
        LinkedHashSet<ZsetEntry> exist = entry.getValue();
        return exist.size();
    }

    public Integer zcount(String key, double min, double max) {
        CacheEntry<?> entry = map.get(key);
        if (entry == null) return null;
        LinkedHashSet<ZsetEntry> exist = (LinkedHashSet<ZsetEntry>) entry.getValue();
        return (int) exist.stream().filter(x -> x.getScore() >= min && x.getScore() <= max).count();
    }

    public Double zscore(String key, String val) {
        CacheEntry<?> entry = map.get(key);
        if (entry == null) return null;
        LinkedHashSet<ZsetEntry> exist = (LinkedHashSet<ZsetEntry>) entry.getValue();
        return exist.stream().filter(x -> x.getValue().equals(val))
                .map(ZsetEntry::getScore).findFirst().orElse(null);
    }

    public Integer zrank(String key, String val) {
        CacheEntry<?> entry = map.get(key);
        if (entry == null) return null;
        LinkedHashSet<ZsetEntry> exist = (LinkedHashSet<ZsetEntry>) entry.getValue();
        Double zscore = zscore(key, val);
        if (zscore == null) return null;
        return (int) exist.stream().filter(x -> x.getScore()< zscore).count();
    }

    public Integer zrem(String key, String[] vals) {
        CacheEntry<?> entry = map.get(key);
        if (entry == null) return null;
        LinkedHashSet<ZsetEntry> exist = (LinkedHashSet<ZsetEntry>) entry.getValue();
        return vals == null ? 0 : (int) Arrays.stream(vals)
                .map(x -> exist.removeIf(y -> y.getValue().equals(x)))
                .filter(x -> x).count();
    }


    //================== 5.zset end ====================


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CacheEntry<T> {
        private T value;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ZsetEntry {
        private String value;
        private double score;
    }

}
