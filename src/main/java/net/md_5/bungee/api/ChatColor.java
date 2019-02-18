package net.md_5.bungee.api;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Lisence BSD 3-Clause "New" or "Revised" License
 * @link https://github.com/SpigotMC/BungeeCord/blob/master/LICENSE
 *
 * Copyright (c) 2012, md_5. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * The name of the author may not be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * You may not use the software for commercial software hosting services without
 * written permission from the author.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
public enum ChatColor
{

    /**
     * Represents black.
     */
    BLACK( '0', "black" ),
    /**
     * Represents dark blue.
     */
    DARK_BLUE( '1', "dark_blue" ),
    /**
     * Represents dark green.
     */
    DARK_GREEN( '2', "dark_green" ),
    /**
     * Represents dark blue (aqua).
     */
    DARK_AQUA( '3', "dark_aqua" ),
    /**
     * Represents dark red.
     */
    DARK_RED( '4', "dark_red" ),
    /**
     * Represents dark purple.
     */
    DARK_PURPLE( '5', "dark_purple" ),
    /**
     * Represents gold.
     */
    GOLD( '6', "gold" ),
    /**
     * Represents gray.
     */
    GRAY( '7', "gray" ),
    /**
     * Represents dark gray.
     */
    DARK_GRAY( '8', "dark_gray" ),
    /**
     * Represents blue.
     */
    BLUE( '9', "blue" ),
    /**
     * Represents green.
     */
    GREEN( 'a', "green" ),
    /**
     * Represents aqua.
     */
    AQUA( 'b', "aqua" ),
    /**
     * Represents red.
     */
    RED( 'c', "red" ),
    /**
     * Represents light purple.
     */
    LIGHT_PURPLE( 'd', "light_purple" ),
    /**
     * Represents yellow.
     */
    YELLOW( 'e', "yellow" ),
    /**
     * Represents white.
     */
    WHITE( 'f', "white" ),
    /**
     * Represents magical characters that change around randomly.
     */
    MAGIC( 'k', "obfuscated" ),
    /**
     * Makes the text bold.
     */
    BOLD( 'l', "bold" ),
    /**
     * Makes a line appear through the text.
     */
    STRIKETHROUGH( 'm', "strikethrough" ),
    /**
     * Makes the text appear underlined.
     */
    UNDERLINE( 'n', "underline" ),
    /**
     * Makes the text italic.
     */
    ITALIC( 'o', "italic" ),
    /**
     * Resets all previous chat colors or formats.
     */
    RESET( 'r', "reset" );
    /**
     * The special character which prefixes all chat colour codes. Use this if
     * you need to dynamically convert colour codes from your custom format.
     */
    public static final char COLOR_CHAR = '\u00A7';
    public static final String ALL_CODES = "0123456789AaBbCcDdEeFfKkLlMmNnOoRr";
    /**
     * Pattern to remove all colour codes.
     */
    public static final Pattern STRIP_COLOR_PATTERN = Pattern.compile( "(?i)" + String.valueOf( COLOR_CHAR ) + "[0-9A-FK-OR]" );
    /**
     * Colour instances keyed by their active character.
     */
    private static final Map<Character, ChatColor> BY_CHAR = new HashMap<Character, ChatColor>();
    /**
     * The code appended to {@link #COLOR_CHAR} to make usable colour.
     */
    private final char code;
    /**
     * This colour's colour char prefixed by the {@link #COLOR_CHAR}.
     */
    private final String toString;
    // @Getter
    private final String name;

    public String getName() {
    	return name;
    }

    static
    {
        for ( ChatColor colour : values() )
        {
            BY_CHAR.put( colour.code, colour );
        }
    }

    private ChatColor(char code, String name)
    {
        this.code = code;
        this.name = name;
        this.toString = new String( new char[]
        {
            COLOR_CHAR, code
        } );
    }

    @Override
    public String toString()
    {
        return toString;
    }

    /**
     * Strips the given message of all color codes
     *
     * @param input String to strip of color
     * @return A copy of the input string, without any coloring
     */
    public static String stripColor(final String input)
    {
        if ( input == null )
        {
            return null;
        }

        return STRIP_COLOR_PATTERN.matcher( input ).replaceAll( "" );
    }

    public static String translateAlternateColorCodes(char altColorChar, String textToTranslate)
    {
        char[] b = textToTranslate.toCharArray();
        for ( int i = 0; i < b.length - 1; i++ )
        {
            if ( b[i] == altColorChar && ALL_CODES.indexOf( b[i + 1] ) > -1 )
            {
                b[i] = ChatColor.COLOR_CHAR;
                b[i + 1] = Character.toLowerCase( b[i + 1] );
            }
        }
        return new String( b );
    }

    /**
     * Get the colour represented by the specified code.
     *
     * @param code the code to search for
     * @return the mapped colour, or null if non exists
     */
    public static ChatColor getByChar(char code)
    {
        return BY_CHAR.get( code );
    }
}