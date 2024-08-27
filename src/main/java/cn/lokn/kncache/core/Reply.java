package cn.lokn.kncache.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: reply for 5 type
 * @author: lokn
 * @date: 2024/08/25 21:30
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Reply<T> {

    T value;
    ReplyType type;

   public static Reply<String> string(String value) {
       return new Reply<String>(value, ReplyType.SIMPLE_STRING);
   }

    public static Reply<String> bulkString(String value) {
        return new Reply<String>(value, ReplyType.BULK_STRING);
    }

    public static Reply<Integer> integer(Integer value) {
        return new Reply<Integer>(value, ReplyType.INT);
    }

    public static Reply<String> error(String value) {
        return new Reply<String>(value, ReplyType.ERROR);
    }

    public static Reply<String[]> array(String[] value) {
        return new Reply<String[]>(value, ReplyType.ARRAY);
    }

}
