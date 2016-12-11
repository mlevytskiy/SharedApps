package com.ns.developer.tagview.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by max on 11.12.16.
 */

public class AutocompleteHelper {

    public static List<String> filter(List<String> allTags, String prefix, int min) {
        if (prefix.length() < min) {
            return new ArrayList<>();
        }

        return filter(allTags, prefix);
    }

    private static List<String> filter(List<String> allTags, String text) {
        List<String> newTags = new ArrayList<>();
        for (String tag : allTags) {
            if (tag.equals(text)) {
                continue;
            }
            if (startWith(text, tag)) {
                newTags.add(tag);
            } else if (hasSubstring(text, tag)) {
                newTags.add(0, tag);
            }
        }
        return newTags;
    }

    private static boolean hasSubstring(String text, String tag) {
        String textUpCase = text.toUpperCase();
        if (tag.toUpperCase().contains(textUpCase)) {
            return true;
        }
        return false;
    }

    private static boolean startWith(String text, String tag) {
        String textUpCase = text.toUpperCase();
        if (tag.toUpperCase().startsWith(textUpCase)) {
            return true;
        }
        return false;
    }

}
