package com.emilkowalczyk;

import java.util.Scanner;

public class DataEncryptionStandard {
    private Scanner userInput = new Scanner(System.in);
    private String plainText = "";
    private String plainTextBinary = "";
    private int plainTextLength;
    private String key = "";
    private String permutatedKey = "";
    private int[] arrayShiftValues = {2, 2, 1, 2, 1, 1, 2, 2, 1, 1, 2, 1, 1, 1, 2, 2};
    private String[] _16Keys_Temporary = new String[16];
    private String permutatedSubkey = "";
    private String[] _16Keys_for_FeistelFunction = new String[16];

    private String finalResult = "";
    private String encipher = "";
    private String textEncipher = "";
    private int leftSpace;
    private String decipher = "";
    private String binaryDecipher = "";
    private String binaryText = "";


    // nazewnictwo tablic wykorzystywanych do permutacji oraz ich wartosci zostala wziete z wikipedii
    private int[] PC_1 = {                                              // 8,16,24 i wielokrotność 8
            13, 49, 41, 33, 46, 18, 9,
            4, 58, 50, 42, 34, 26, 23,
            38, 2, 59, 51, 43, 35, 27,
            19, 11, 3, 60, 52, 44, 36,
            63, 21, 54, 39, 15, 17, 31,
            7, 62, 47, 25, 10, 30, 22,
            14, 6, 61, 53, 45, 37, 29,
            55, 57, 5, 28, 20, 12, 1};

    private int[] PC_2 = {
            14, 17, 11, 24, 1, 5,
            3, 28, 15, 6, 21, 10,
            23, 19, 12, 4, 26, 8,
            16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55,
            30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53,
            46, 42, 50, 36, 29, 32};
    int[] IP = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7};
    int[] FP = {
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25};
    int[] E = {
            32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1};
    int[][] S1 = {
            {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
            {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
            {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
            {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}};

    int[][] S2 = {
            {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
            {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
            {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
            {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}};

    int[][] S3 = {
            {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
            {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
            {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
            {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}};

    int[][] S4 = {
            {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
            {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
            {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
            {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}};

    int[][] S5 = {
            {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
            {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
            {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
            {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}};

    int[][] S6 = {
            {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
            {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
            {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
            {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}};

    int[][] S7 = {
            {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
            {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
            {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
            {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}};

    int[][] S8 = {
            {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
            {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
            {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
            {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}};

    int[] P = {16, 7, 20, 21,
            29, 12, 28, 17,
            1, 15, 23, 26,
            5, 18, 31, 10,
            2, 8, 24, 14,
            32, 27, 3, 9,
            19, 13, 30, 6,
            22, 11, 4, 25};


    public void enterPlainText(String plainText) {

        try {

            //System.out.println("Enter a text to encrypt, it has to be at least 8 characters (64 bits).");
            //plainText = userInput.nextLine();
            System.out.println("Your text: " + plainText);
            binaryText = stringToBinary(plainText);
            System.out.println("Your text in binary notion: " + binaryText);
            binaryText = binaryText.replace(" ", "");

            plainTextLength = binaryText.length();
            if (plainTextLength < 64) {
                System.out.println("The text entered by you is too short!");
                System.exit(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enterKey(String inputKey) {
        try {
            //  System.out.println("\n\nPlease enter the key. The key must consist of 64 bits (8 characters).");
            System.out.println("The key: " + inputKey);
            String binaryKey = stringToBinary(inputKey);

            System.out.println("The Key in binary notion: " + binaryKey);
            binaryKey = binaryKey.replace(" ", "");

            for (int i : PC_1) {
                permutatedKey += binaryKey.charAt(i - 1);   // Do szyfrowania i deszyfrowania danych wykorzystywanych jest 56 bitów klucza,
                // ponieważ co 8 bit jest bitem kontrolnym

            }
            System.out.println("56-bit key:   " + permutatedKey.replaceAll("(.{8})(?!$)", "$1 "));

            String leftPermutatedKey = permutatedKey.substring(0, 28);
            String rightPermutatedKey = permutatedKey.substring(28, 56);
            String leftPermutatedKeyShifted = leftPermutatedKey;
            String rightPermutatedKeyShifted = rightPermutatedKey;
            System.out.println("\n");
            int Index = 0;
            for (int a : arrayShiftValues) {
                leftPermutatedKeyShifted = shiftBits(leftPermutatedKeyShifted, a);
                rightPermutatedKeyShifted = shiftBits(rightPermutatedKeyShifted, a);
                _16Keys_Temporary[Index] = leftPermutatedKeyShifted + rightPermutatedKeyShifted;
                Index++;
            }

            Index = 0;
            for (String key : _16Keys_Temporary) {
                for (int j : PC_2) {
                    permutatedSubkey += key.charAt(j - 1);
                }
                _16Keys_for_FeistelFunction[Index] = permutatedSubkey; // keys for Feistel Function are 48-bit long
                Index++;
                permutatedSubkey = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String encryptDES() {
        try {
            System.out.println("\n\n\n");
            int leftpadString = plainTextLength % 64;
            int loop = leftpadString != 0 ? plainTextLength / 64 + 1 : plainTextLength / 64;
            int start = 0;
            int end = 64;
            for (int id = loop, wordCounter = 1; id > 0; id--, wordCounter++) {
                int leftpad = 64 - leftpadString;
                leftSpace = leftpad % 64 != 0 ? leftpad / 8 : 0;
                end = plainTextLength - start < 64 ? plainTextLength : end;
                plainTextBinary = binaryText.substring(start, end);
                while (plainTextBinary.length() != 64) {
                    plainTextBinary = "0" + plainTextBinary;
                }

                String IPBinary = "";
                for (int i : IP) {
                    IPBinary += plainTextBinary.charAt(i - 1);
                }

                String LeftIPBinary = IPBinary.substring(0, 32);
                String RightIPBinary = IPBinary.substring(32, 64);


                //Sieć Feistela
                int counter = 1;
                for (String k : _16Keys_for_FeistelFunction) {
                    System.out.println("64-bit " + wordCounter + " ENCRYPTION ROUND " + counter + "                            ");
                    System.out.println("KEY = " + k);

                    String LeftBlock = RightIPBinary;
                    System.out.println("Left block  = " + LeftBlock);

                    String expand = "";
                    for (int i : E) {
                        expand += RightIPBinary.charAt(i - 1);
                    }

                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < k.length(); i++) {
                        sb.append((k.charAt(i) ^ expand.charAt(i))); //XOR
                    }
                    String result = sb.toString();

                    String RB1 = result.substring(0, 6);
                    String row1 = String.valueOf(RB1.substring(0, 1) + RB1.substring(5, 6));
                    String col1 = String.valueOf(RB1.substring(1, 5));
                    int target = S1[Integer.parseInt(row1, 2)][Integer.parseInt(col1, 2)];
                    String binaryTarget = String.format("%4s", Integer.toBinaryString(target)).replace(' ', '0');

                    String RB2 = result.substring(6, 12);
                    row1 = String.valueOf(RB2.substring(0, 1) + RB2.substring(5, 6));
                    col1 = String.valueOf(RB2.substring(1, 5));
                    target = S2[Integer.parseInt(row1, 2)][Integer.parseInt(col1, 2)];
                    binaryTarget += String.format("%4s", Integer.toBinaryString(target)).replace(' ', '0');

                    String RB3 = result.substring(12, 18);
                    row1 = String.valueOf(RB3.substring(0, 1) + RB3.substring(5, 6));
                    col1 = String.valueOf(RB3.substring(1, 5));
                    target = S3[Integer.parseInt(row1, 2)][Integer.parseInt(col1, 2)];
                    binaryTarget += String.format("%4s", Integer.toBinaryString(target)).replace(' ', '0');

                    String RB4 = result.substring(18, 24);
                    row1 = String.valueOf(RB4.substring(0, 1) + RB4.substring(5, 6));
                    col1 = String.valueOf(RB4.substring(1, 5));
                    target = S4[Integer.parseInt(row1, 2)][Integer.parseInt(col1, 2)];
                    binaryTarget += String.format("%4s", Integer.toBinaryString(target)).replace(' ', '0');

                    String RB5 = result.substring(24, 30);
                    row1 = String.valueOf(RB5.substring(0, 1) + RB5.substring(5, 6));
                    col1 = String.valueOf(RB5.substring(1, 5));
                    target = S5[Integer.parseInt(row1, 2)][Integer.parseInt(col1, 2)];
                    binaryTarget += String.format("%4s", Integer.toBinaryString(target)).replace(' ', '0');

                    String RB6 = result.substring(30, 36);
                    row1 = String.valueOf(RB6.substring(0, 1) + RB6.substring(5, 6));
                    col1 = String.valueOf(RB6.substring(1, 5));
                    target = S6[Integer.parseInt(row1, 2)][Integer.parseInt(col1, 2)];
                    binaryTarget += String.format("%4s", Integer.toBinaryString(target)).replace(' ', '0');

                    String RB7 = result.substring(36, 42);
                    row1 = String.valueOf(RB7.substring(0, 1) + RB7.substring(5, 6));
                    col1 = String.valueOf(RB7.substring(1, 5));
                    target = S7[Integer.parseInt(row1, 2)][Integer.parseInt(col1, 2)];
                    binaryTarget += String.format("%4s", Integer.toBinaryString(target)).replace(' ', '0');

                    String RB8 = result.substring(42, 48);
                    row1 = String.valueOf(RB8.substring(0, 1) + RB8.substring(5, 6));
                    col1 = String.valueOf(RB8.substring(1, 5));
                    target = S8[Integer.parseInt(row1, 2)][Integer.parseInt(col1, 2)];
                    binaryTarget += String.format("%4s", Integer.toBinaryString(target)).replace(' ', '0');

                    String function = "";
                    for (int d : P) {                           // ostatnia permutacja, z użyciem tablicy P
                        function += binaryTarget.charAt(d - 1);
                    }


                    sb = new StringBuilder();
                    for (int i = 0; i < LeftIPBinary.length(); i++) {
                        sb.append((LeftIPBinary.charAt(i) ^ function.charAt(i))); //operacja XOR
                    }
                    result = sb.toString();

                    RightIPBinary = result;
                    System.out.println("RIGHT BLOCK = " + RightIPBinary);
                    result = "";

                    counter++;
                    if (counter > 16) {
                        result = RightIPBinary + LeftBlock;

                        finalResult = "";
                        for (int x : FP) {
                            finalResult += result.charAt(x - 1);
                        }
                        encipher += finalResult;
                        textEncipher += id == 1 && leftSpace != 0 ? integerToString(finalResult, 8).substring(leftSpace) : integerToString(finalResult, 8);
                    }
                    LeftIPBinary = LeftBlock;
                }
                end += 64;
                start += 64;
                System.out.println("CIPHER OF 64-bit " + wordCounter + " = " + integerToString(finalResult, 8));
                System.out.println("\n\n");
            }
            //Wyświetlanie zaszyfrowanego tekst jawny
            System.out.println("\nCIPHER = " + encipher.replaceAll("(.{8})(?!$)", "$1 "));
            System.out.println("CIPHER IN PLAIN TEXT= " + textEncipher + "\n\n\n");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return   encipher.replaceAll("(.{8})(?!$)", "$1 ").toString();
    }

    public void decryptDES() {

        try {

            binaryText = encipher;
            int encipherLength = encipher.length();
            int leftpadString = encipherLength % 64;
            int loop = leftpadString != 0 ? encipherLength / 64 + 1 : encipherLength / 64;
            int start = 0;
            int end = 64;
            System.out.println("_________________________________________________________________________________");
            for (int id = loop, wordCount = 1; id > 0; id--, wordCount++) {
                int leftpad = 64 - leftpadString;
                end = encipherLength - start < 64 ? encipherLength : end;
                String cipherTextBinary = binaryText.substring(start, end);

                String IPBinary = "";
                for (int i : IP) {
                    IPBinary += cipherTextBinary.charAt(i - 1);
                }

                String LeftIPBinary = IPBinary.substring(0, 32);
                String RightIPBinary = IPBinary.substring(32, 64);

                // przejście prze 1
                int counter = 1;
                String k;
                for (int p = 15; p >= 0; p--) {
                    System.out.println();
                    System.out.println("                    64-bit " + wordCount + " DECRYPTION ROUND " + counter + "                            ");
                    k = _16Keys_for_FeistelFunction[p];
                    System.out.println("KEY = " + k);

                    String LeftBlock = RightIPBinary;
                    System.out.println("LEFT BLOCK  = " + LeftBlock);

                    String expand = "";
                    for (int i : E) {
                        expand += RightIPBinary.charAt(i - 1);
                    }

                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < k.length(); i++) {
                        sb.append((k.charAt(i) ^ expand.charAt(i))); //operacja XOR. łaczenie bitów klucza z bitami danych. wyniku powstaje 48 bitów
                    }
                    String result = sb.toString();

                    String RB1 = result.substring(0, 6);
                    String row1 = String.valueOf(RB1.substring(0, 1) + RB1.substring(5, 6));
                    String col1 = String.valueOf(RB1.substring(1, 5));
                    int target = S1[Integer.parseInt(row1, 2)][Integer.parseInt(col1, 2)];
                    String binaryTarget = String.format("%4s", Integer.toBinaryString(target)).replace(' ', '0');

                    String RB2 = result.substring(6, 12);
                    row1 = String.valueOf(RB2.substring(0, 1) + RB2.substring(5, 6));
                    col1 = String.valueOf(RB2.substring(1, 5));
                    target = S2[Integer.parseInt(row1, 2)][Integer.parseInt(col1, 2)];
                    binaryTarget += String.format("%4s", Integer.toBinaryString(target)).replace(' ', '0');

                    String RB3 = result.substring(12, 18);
                    row1 = String.valueOf(RB3.substring(0, 1) + RB3.substring(5, 6));
                    col1 = String.valueOf(RB3.substring(1, 5));
                    target = S3[Integer.parseInt(row1, 2)][Integer.parseInt(col1, 2)];
                    binaryTarget += String.format("%4s", Integer.toBinaryString(target)).replace(' ', '0');

                    String RB4 = result.substring(18, 24);
                    row1 = String.valueOf(RB4.substring(0, 1) + RB4.substring(5, 6));
                    col1 = String.valueOf(RB4.substring(1, 5));
                    target = S4[Integer.parseInt(row1, 2)][Integer.parseInt(col1, 2)];
                    binaryTarget += String.format("%4s", Integer.toBinaryString(target)).replace(' ', '0');

                    String RB5 = result.substring(24, 30);
                    row1 = String.valueOf(RB5.substring(0, 1) + RB5.substring(5, 6));
                    col1 = String.valueOf(RB5.substring(1, 5));
                    target = S5[Integer.parseInt(row1, 2)][Integer.parseInt(col1, 2)];
                    binaryTarget += String.format("%4s", Integer.toBinaryString(target)).replace(' ', '0');

                    String RB6 = result.substring(30, 36);
                    row1 = String.valueOf(RB6.substring(0, 1) + RB6.substring(5, 6));
                    col1 = String.valueOf(RB6.substring(1, 5));
                    target = S6[Integer.parseInt(row1, 2)][Integer.parseInt(col1, 2)];
                    binaryTarget += String.format("%4s", Integer.toBinaryString(target)).replace(' ', '0');

                    String RB7 = result.substring(36, 42);
                    row1 = String.valueOf(RB7.substring(0, 1) + RB7.substring(5, 6));
                    col1 = String.valueOf(RB7.substring(1, 5));
                    target = S7[Integer.parseInt(row1, 2)][Integer.parseInt(col1, 2)];
                    binaryTarget += String.format("%4s", Integer.toBinaryString(target)).replace(' ', '0');

                    String RB8 = result.substring(42, 48);
                    row1 = String.valueOf(RB8.substring(0, 1) + RB8.substring(5, 6));
                    col1 = String.valueOf(RB8.substring(1, 5));
                    target = S8[Integer.parseInt(row1, 2)][Integer.parseInt(col1, 2)];
                    binaryTarget += String.format("%4s", Integer.toBinaryString(target)).replace(' ', '0');


                    // Wyjście ze S-bloków poddawane jest permutacji  w P blokach
                    String function = "";
                    for (int d : P) {
                        function += binaryTarget.charAt(d - 1);
                    }

                    //Bity tak przekształconego bloku są poddawane funkcji XOR z bitami lewej połowy danych
                    sb = new StringBuilder();
                    for (int i = 0; i < LeftIPBinary.length(); i++) {
                        sb.append((LeftIPBinary.charAt(i) ^ function.charAt(i)));
                    }
                    result = sb.toString();

                    RightIPBinary = result;
                    System.out.println("RIGHT BLOCK = " + RightIPBinary);
                    result = "";

                    counter++;
                    if (counter > 16) {

                        result = RightIPBinary + LeftBlock;


                        String finalResult = "";
                        for (int x : FP) {
                            finalResult += result.charAt(x - 1);
                        }

                        binaryDecipher += finalResult;
                        decipher += id == 1 && leftSpace != 0 ? integerToString(finalResult, 8).substring(leftSpace) : integerToString(finalResult, 8);
                        System.out.println("DECRYPTED CIPHER OF 64-bit " + wordCount + " = " + integerToString(finalResult, 8));
                    }
                    LeftIPBinary = LeftBlock;
                }

                end += 64;
                start += 64;
                System.out.println("_________________________________________________________________________________");
            }

            System.out.println("\nDECRYPTED CIPHER = " + binaryDecipher.replaceAll("(.{8})(?!$)", "$1 "));
            System.out.println("DECRYPTED CIPHER IN PLAIN TEXT = " + decipher);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String stringToBinary(String str ) {

        byte[] bytes = str.getBytes();
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
            binary.append(' ');
        }
        return binary.toString();
    }

    public String integerToString(String stream, int size ) {

        String result = "";
        for (int i = 0; i < stream.length(); i += size) {
            result += (stream.substring(i, Math.min(stream.length(), i + size)) + " ");
        }
        String[] ss = result.split( " " );
        StringBuilder sb = new StringBuilder();
        for ( int i = 0; i < ss.length; i++ ) {
            sb.append( (char)Integer.parseInt( ss[i], 2 ) );
        }
        return sb.toString();
    }

    String shiftBits(String word, int offset) {

        String result = word.substring(offset);
        for (int i = 0; i < offset; i++) {
            result += word.charAt(i);
        }
        return result;
    }

}