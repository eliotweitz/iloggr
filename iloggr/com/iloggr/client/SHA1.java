/**
 * Copyright (C) 2009 Bump Mobile Inc.
 * All rights reserved.
 */
package com.iloggr.client;

/**
 * Date: Oct 15, 2007
 */

/******************************************************************************
 *
 * Copyright (c) 1998-2000 by Mindbright Technology AB, Stockholm, Sweden.
 *                 www.mindbright.se, info@mindbright.se
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *****************************************************************************
 * $Author: nallen $
 * $Date: 2001/11/12 16:31:16 $
 * $Name:  $
 *****************************************************************************/

/**
*   This class calculates an SHA-1 hashsum.
*   The code is similar to the one presented in
*   "Kryptographie, Dietmar Waetjen, Spektrum Verlag, 2004"
*   and has neither performace optimizations nor stream support
*
*   @author Andreas Warnke
*
*/
/*
*   History:
*   2005-06-15 hash: modulo on negative values replaced by bitwise-and; Point 1) solved.
*   2005-06-14 Review by A. Warnke; Findings:
*              1) possibly(?) wrong result if calculating modulo on negative values
*   2004-10-31 Singleton: is never null
*   2004-05-07 comments updated, code review by A. Warnke, 2 tests of doSHA1()
*   2004-05-06 class created
*/
/*
*   RETE-DB (formerly ACCS) Access controlling database web-frontend
*   Copyright (C) 2004,2005 Andreas Warnke
*
*   This program is free software; you can redistribute it and/or modify
*   it under the terms of the GNU General Public License as published by
*   the Free Software Foundation; either version 2 of the License, or
*   (at your option) any later version.
*
*   This program is distributed in the hope that it will be useful,
*   but WITHOUT ANY WARRANTY; without even the implied warranty of
*   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*   GNU General Public License for more details.
*
*   You should have received a copy of the GNU General Public License
*   along with this program; if not, write to the Free Software
*   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*
*   How to person the author:
*   Write a mail to RETE-DB at andreaswarnke.de
*/

public final class SHA1 {
    /**
    *   gets the SHA1 singleton object
    */
    public static SHA1 getInstance () {
        return Singleton;
    }

    /** Reference to the only SHA1 object */
    private static SHA1 Singleton = new SHA1();

    /**
    *   private constructor
    */
    private SHA1 () {
    	// utility
    }

    /**
    *   this function converts a plain string to a hash string.
    *   The unicode string is converted to a byte array in
    *   big-endian order; the SHA1 is then executed.
    *   @param plain the original message
    *   @return hexadecimal number as string, 40 characters
    */
    public String hash ( String plain) {
        //  do the precalculation:
        int bit_length = plain.length() * 16;  //  16 is the unicode char bitlength
        int total_bit_length = ( bit_length + 1 + 64 + 511 ) & -512; // int d = ( 447 - bit_length) % 512;
        int msg_length = total_bit_length/32; // ( bit_length + 1 + d + 64 ) / 32;  //  number of 32 bit integers
        int[] Message = new int[ msg_length];  //  should be initialized with zeros

        //  fill the Message integers:
        for ( int pos = 0; pos < msg_length; pos ++ ) {
            //  higher 16 bits:
            if ( pos*32 < bit_length) {
                int unicode = plain.charAt( pos*2);
                Message[pos] = ( unicode << 16);
            }
            else if ( pos*32 == bit_length)
                Message[pos] = 0x80000000;
            //  lower 16 bits
            if ( (pos*32+16) < bit_length) {
                int unicode2 = plain.charAt( pos*2+1);
                Message[pos] = Message[pos] | ( unicode2 & 0x0000ffff);
            }
            else if ( (pos*32+16) == bit_length)
                Message[pos] = Message[pos] | 0x00008000;
        }
        Message[msg_length-1] = bit_length;

        //  test code:
        //int[] test = new int [16];
        //test[0]=0x61626380; //  aBump
        //test[15]=0x00000018;  // length = 24 bit = 0x18;
        //if ( 1==1) return doSHA1( test);
        // "aBump" ->
        // A9993E36 4706816A BA3E2571 7850C26C 9CD0D89D
        //String tests="aBumpdBumpdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq"; // 56 chars
        //int[] test = new int [32];  //  56/4=14
        //for ( int pos = 0; pos < 14; pos ++)
        //    for ( int i = 0; i < 4; i ++ )
        //        test[pos]|=(((int)tests.charAt(pos*4+i))&0xff)<<((3-i)*8);
        //test[14]=0x80000000;
        //test[31]=56*8;  // length = 56*8 bit;
        //if ( 1==1) return doSHA1( test);
        // "aBumpdBumpdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq" ->
        // 84983E44 1C3BD26E BAAE4AA1 F95129E5 E54670F1

        //  digest:
        return doSHA1( Message);

    }

    //  required constants:
    private static final int h1 = 0x67452301;
    private static final int h2 = 0xefcdab89;
    private static final int h3 = 0x98badcfe;
    private static final int h4 = 0x10325476;
    private static final int h5 = 0xc3d2e1f0;
    private static final int y1 = 0x5a827999;
    private static final int y2 = 0x6ed9eba1;
    private static final int y3 = 0x8f1bbcdc;
    private static final int y4 = 0xca62c1d6;

    /** rotate left */
    private static int rol ( int x, int shift ) {
        return (( x << shift ) | ( x >>> ( 32-shift )));
    }

    /**
    *   this method calculates the SHA1 digest
    *
    *   @param Message precalculated array on 32 bit values;
    *       the length is a multiple of 16, the bit after
    *       the last message bit is set, the last 64 bits
    *       represent the message length in big endian order
    *   @return hexadecimal representation of the result, 40 characters
    */
    private String doSHA1 ( int[] Message ) {
         int H1 = SHA1.h1;
         int H2 = SHA1.h2;
         int H3 = SHA1.h3;
         int H4 = SHA1.h4;
         int H5 = SHA1.h5;
         int integer_count = Message.length;
         int[] X = new int[80];
         int j;
         for ( int block_start = 0; block_start < integer_count; block_start += 16 ) {
             //  init X:
             for ( j = 0; j < 16; j ++ )
                 X[j] = Message[block_start+j];
             for ( j = 16; j < 80; j ++ )
                 X[j] = rol(X[j-3]^X[j-8]^X[j-14]^X[j-16],1);

             //  calculate A,B,C,D,E:
             int A=H1; int B=H2; int C=H3; int D=H4; int E=H5; int t;
             for ( j = 0; j < 20; j ++ ) {
                 t = rol(A,5)+((B&C)|((~B)&D))+E+X[j]+y1;
                 E=D; D=C; C=rol(B,30); B=A; A=t;
             }
             for ( j = 20; j < 40; j ++ ) {
                 t = rol(A,5)+(B^C^D)+E+X[j]+y2;
                 E=D; D=C; C=rol(B,30); B=A; A=t;
             }
             for ( j = 40; j < 60; j ++ ) {
                 t = rol(A,5)+((B&C)|(B&D)|(C&D))+E+X[j]+y3;
                 E=D; D=C; C=rol(B,30); B=A; A=t;
             }
             for ( j = 60; j < 80; j ++ ) {
                 t = rol(A,5)+(B^C^D)+E+X[j]+y4;
                 E=D; D=C; C=rol(B,30); B=A; A=t;
             }
             H1+=A; H2+=B; H3+=C; H4+=D; H5+=E;
         }

        //  convert result to hexadecimal string:
        StringBuffer result = new StringBuffer ( 40);
        intToHex( H1, result);
        intToHex( H2, result);
        intToHex( H3, result);
        intToHex( H4, result);
        intToHex( H5, result);
        return result.toString();
    }

    /**
    *   converts an int number to hexadecimal representation
    */
    private static void intToHex( int x, StringBuffer out) {
        for ( int pos = 0; pos < 8; pos ++ )
            out.append( intToChar((x>>>(28-(pos*4)))&0xf));
    }

     /**
    *   converts an int number between 0 and 36 to a char (base 36)
    *   <br />
    *   Note: the result is undefined, if parameter is out of range
    *   @todo to improve performance, implement a table-lookup instead of if/then cases
    */
    private static char intToChar( int zeroTo36) {
        if ( zeroTo36 <= 9)
            if ( zeroTo36 >= 0)
                return (char)( zeroTo36 + '0');
            else
                return 0;
        else
            if ( zeroTo36 < 36)
                return (char)( zeroTo36-10 + 'a');
            else
                return 0;
    }

    public static final long hash(long h) {
          // Spread bits to regularize both segment and index locations,
        // using variant of single-word Wang/Jenkins hash.
         h += (h <<  15) ^ 0xffffcd7d;
         h ^= (h >>> 10);
         h += (h <<   3);
         h ^= (h >>>  6);
         h += (h <<   2) + (h << 14);
         return h ^ (h >>> 16);  
     }
    

}