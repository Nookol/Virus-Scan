package com.company;
import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        List<String> results = new ArrayList<>();
        String hex;
        File[] files = new File("D:\\VST").listFiles();
        ArrayList<String> Hlist;
        assert files != null;
        for (File file : files) {
            if (file.isFile()) {
                results.add(file.getName());
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                Hlist = new ArrayList<>();
                hex = checksum(String.valueOf((file)), md);
                Hlist.add(hex);
                System.out.println(results);
                System.out.println(Hlist.stream().toList());
                Compare(Hlist, getVirus(), String.valueOf(results));
                results.remove(file.getName());
                Hlist.remove(hex);
            }
        }
    }

    private static String checksum(String filepath, MessageDigest md) throws IOException {
        try (DigestInputStream dis = new DigestInputStream(new FileInputStream(filepath), md)) {
            while (dis.read() != -1) ;
            md = dis.getMessageDigest();
        }
        StringBuilder result = new StringBuilder();
        for (byte b : md.digest()) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    private static void Compare(ArrayList<String> a, ArrayList<String> b, String j) {
        for (String s : a) {
            for (String s1 : b) {
                if (s.equals(s1)) {
                    System.out.println("Virus");
                    try {
                        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("NewScannedVirus.txt", true)));
                        out.println(j + " - is a Virus\n");
                        out.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        }
    }

    private static ArrayList<String> getVirus() throws IOException {
        BufferedReader bufReader = new BufferedReader(new FileReader("FILE PATH OF KNOWN VIRUS CSV/TXT"));
        ArrayList<String> listOfLines = new ArrayList<>();
        String line = bufReader.readLine();
        while (line != null) {
            line = line.trim();
            listOfLines.add(line);
            line = bufReader.readLine();
        }
        bufReader.close();
        return listOfLines;
    }
}