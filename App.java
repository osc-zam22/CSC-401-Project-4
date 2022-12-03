// Juan Suria
// Tyler Buckley
// Yorman Tevalan
// Oscar Zambrano
// Eduardo Beltran
/*
Write a program that constructs a Huffman code for a given English text and encode it. The English character occurrence probabilities can be computed from the given text.
Experiment with your encoding program to find a range of typical compression ratios for Huffman’s encoding of English texts of, say, 1000 words.
Experiment with your encoding program to find out how sensitive the compression ratios are to using standard estimates of frequencies instead of actual frequencies of character occurrences in English texts.
Write a program for decoding an English text that has been encoded with a Huffman code. You can design your way to communicate the English character occurrence probabilities.
*/
import java.util.*;

class Node {
    int freq;
    char character;

    Node left = null;
    Node right = null;

    public Node() {

    }

    public Node(Character character, Integer freq) {
        this.freq = freq;
        this.character = character;
    }
}

public class App {

    // Function that reads the text and count the frequency of every character
    public static Set<Map.Entry<Character, Integer>> getCharactersFrequency(String text) {
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : text.toCharArray()) {
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }
        return freq.entrySet();
    }

    // Recursive function that traverse the tree and generate the codes for each
    // character
    public static void huffmanCodes(Node root, String text, Map<Character, String> codes) {
        if (root == null) {
            return;
        }

        if (root.left == null && root.right == null) {
            System.out.println(root.character + " : " + text);
            codes.put(root.character, text);
            return;
        }

        huffmanCodes(root.left, text + "0", codes);
        huffmanCodes(root.right, text + "1", codes);
    }

    // Function that encode the text
    public static StringBuilder encodeText(Map<Character, String> codes, String text) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            result.append(codes.get(c));
        }
        return result;
    }

    public static int _decode(Node root, int index, StringBuilder encodedText, StringBuilder result) {
        if (root == null) {
            return index;
        }

        if (root.left == null && root.right == null) {
            result.append(root.character);
            return index;
        }

        index++;

        root = (encodedText.charAt(index) == '0') ? root.left : root.right;
        index = _decode(root, index, encodedText, result);
        return index;
    }

    public static StringBuilder decodeText(Node root, StringBuilder encodedText) {
        StringBuilder result = new StringBuilder();

        int index = -1;
        while (index < encodedText.length() - 1) {
            index = _decode(root, index, encodedText, result);
        }

        return result;
    }

    public static void main(String[] args) {

        System.out.println("Input text: ");
        Scanner sc = new Scanner(System.in);

        String text = sc.nextLine();

        Set<Map.Entry<Character, Integer>> freq = getCharactersFrequency(text);

        int totalCharacters = freq.stream().mapToInt(Map.Entry::getValue).sum();

        System.out.println();
        for (var entry : freq) {
            System.out.println("Probabilities of " + entry.getKey() + " = "
                    + String.format("%.3f", ((double) entry.getValue() / totalCharacters)));
        }
        System.out.println();

        // Priority queue with priority in the lowest frequency
        PriorityQueue<Node> q = new PriorityQueue<>(Comparator.comparingInt(n -> n.freq));

        for (var entry : freq) {
            q.add(new Node(entry.getKey(), entry.getValue()));
        }

        Node root = null;

        if (q.size() == 1) {
            System.out.println("\nThe text has only one type of character");
            return;
        }

        while (q.size() > 1) {

            Node first = q.peek();
            q.poll();

            Node second = q.peek();
            q.poll();

            Node f = new Node();

            f.freq = first.freq + second.freq;
            f.character = '-';

            f.left = first;
            f.right = second;

            root = f;

            q.add(f);
        }

        // Map that will save the codes
        Map<Character, String> codes = new HashMap<>();

        huffmanCodes(root, "", codes);

        var encodedText = encodeText(codes, text);
        System.out.println("\nEncoded: " + encodedText);

        var decodedText = decodeText(root, encodedText);
        System.out.println("\nDecoded: " + decodedText);
    }

}