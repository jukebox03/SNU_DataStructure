import java.io.*;
import java.util.*;

public class Matching
{
    public static ArrayList<String> file = null;
    public static String path = null;
    public static int lineCount = 0;
    public static Hashtable<Integer, AVLTree> table = null;

    public static void main(String args[]){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            try{
                String input = br.readLine();
                if (input.compareTo("QUIT") == 0)
                    break;
                command(input);
            }
            catch (IOException e){
                System.out.println(e.toString());
            }
        }
    }
    private static void command(String input) {
        switch (input.charAt(0)){
            case '<':
                setData(input.substring(2));
                break;
            case '@':
                printData(input.substring(2));
                break;
            case '?':
                searchPattern(input.substring(2));
                break;
            case '/':
                deleteString(input.substring(2));
                break;
            case '+':
                addString(input.substring(2));
                break;
            default:
                System.out.println("Wrong Method");
                break;
        }
    }
    private static void setData(String input) {
        try {
            file = new ArrayList<String>();
            path = input;
            BufferedReader reader = new BufferedReader(new FileReader(input));
            String line = null;
            while((line=reader.readLine()) != null){
                lineCount++;
                file.add(line);
            }
            table = new Hashtable<Integer, AVLTree>(100);
            for(int i=0; i<file.size(); i++){
                for(int j=0; j<file.get(i).length()-5; j++){
                    String substring = file.get(i).substring(j, j+6);
                    int key = hashFunction(substring);
                    if(table.containsKey(key)){
                        AVLNode temp = table.get(key).search(substring);
                        if(temp == AVLTree.NIL) table.get(key).insert(substring, i+1, j+1);
                        else{
                            temp.locList.add(new pairInt(i+1, j+1));
                        }
                    }else{
                        AVLTree newTree = new AVLTree();
                        newTree.insert(substring, i+1, j+1);
                        table.put(key, newTree);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void printData(String input) {
        int index = Integer.parseInt(input);
        if(table.containsKey(index) == false) System.out.println("EMPTY");
        else table.get(index).print();
    }
    private static void searchPattern(String input) {
        ArrayList<pairInt> locList = new ArrayList<pairInt>();
        int key = hashFunction(input.substring(0, 6));
        String substring = input.substring(0, 6);
        if(table.containsKey(key) == false){
            System.out.println("(0, 0)");
            return;
        }else{
            AVLNode search = table.get(key).search(substring);
            if(search != AVLTree.NIL) {
                ArrayList<pairInt> temp = search.locList.copyList();
                for (pairInt i : temp) {
                    locList.add(new pairInt(i.i, i.j));
                }
            }
        }
        for(int i=1; i<input.length()-5; i++){
            substring = input.substring(i, i+6);
            key = hashFunction(substring);
            if(table.containsKey(key) == false){
                System.out.println("(0, 0)");
                return;
            }else{
                ArrayList<pairInt> temp = new ArrayList<pairInt>();
                AVLNode search = table.get(key).search(substring);
                if(search != AVLTree.NIL) {
                    ArrayList<pairInt> copy = table.get(key).search(substring).locList.copyList();
                    for (pairInt j : copy) {
                        temp.add(new pairInt(j.i, j.j-i));
                    }
                    for (int l = 0; l < locList.size(); l++) {
                        int t = 0;
                        while (t < temp.size() && !locList.get(l).equals(temp.get(t))) t++;
                        if (t == temp.size()) {
                            locList.remove(l);
                            l--;
                        }
                    }
                }
            }
        }
        if(locList.isEmpty()) System.out.println("(0, 0)");
        else{
            System.out.print("("+locList.get(0).i+", "+locList.get(0).j+")");
            for(int i=1; i<locList.size(); i++){
                System.out.print(" ");
                System.out.print("("+locList.get(i).i+", "+locList.get(i).j+")");
            }
            System.out.println();
        }
    }
    private static void deleteString(String input) {
        ArrayList<Integer>[] locList = new ArrayList[lineCount];
        int key = hashFunction(input);
        int deleteCount = 0;
        if(table.containsKey(key)) {
            AVLNode search = table.get(key).search(input);
            if (search != AVLTree.NIL) {
                ArrayList<pairInt> tempList = search.locList.copyList();
                deleteCount = tempList.size();
                for (int i = 0; i < lineCount; i++) {
                    locList[i] = new ArrayList<Integer>();
                }
                for (pairInt t : tempList) {
                    for (int i = 0; i < 6; i++) {
                        locList[t.i - 1].add(t.j + i - 1);
                    }
                }
                for (int i = 0; i < lineCount; i++) {
                    HashSet<Integer> hs = new HashSet<Integer>(locList[i]);
                    locList[i].clear();
                    locList[i].addAll(hs);
                    Collections.sort(locList[i]);
                }

                for (int i = 0; i < lineCount; i++) {
                    if (locList[i].isEmpty() == false) {
                        int startIndex = 0;
                        if (locList[i].indexOf(0) > 5) startIndex = locList[i].indexOf(0) - 5;
                        for (int j = startIndex; j < file.get(i).length() - 5; j++) {
                            String substring = file.get(i).substring(j, j + 6);
                            key = hashFunction(substring);
                            AVLNode temp = table.get(key).search(substring);
                            ArrayList<pairInt> copyLocList = temp.locList.copyList();
                            int n = 0;
                            while (n < copyLocList.size() && !copyLocList.get(n).equals(new pairInt(i + 1, j + 1))) n++;
                            copyLocList.remove(n);
                            temp.locList.clear();
                            for (int k = 0; k < copyLocList.size(); k++) {
                                temp.locList.add(copyLocList.get(k));
                            }
                            if (temp.locList.isEmpty()) {
                                table.get(key).delete(substring);
                            }
                            if (table.get(key).isEmpty()) {
                                table.remove(key);
                            }
                        }

                        int count = 0;
                        for (Integer num : locList[i]) {
                            String temp = file.get(i);
                            file.remove(i);
                            file.add(i, temp.substring(0, num - count) + temp.substring(num - count + 1));
                            count++;
                        }
                        if(file.get(i).length()>=6) {
                            for (int j = startIndex; j < file.get(i).length() - 5; j++) {
                                String substring = file.get(i).substring(j, j + 6);
                                key = hashFunction(substring);
                                if (table.containsKey(key)) {
                                    AVLNode temp = table.get(key).search(substring);
                                    if (temp == AVLTree.NIL) table.get(key).insert(substring, i + 1, j + 1);
                                    else {
                                        temp.locList.add(new pairInt(i + 1, j + 1));
                                    }
                                } else {
                                    AVLTree newTree = new AVLTree();
                                    newTree.insert(substring, i + 1, j + 1);
                                    table.put(key, newTree);
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println(deleteCount);
    }
    private static void addString(String input) {
        lineCount++;
        file.add(input);
        for(int j=0; j<input.length()-5; j++){
            String substring = input.substring(j, j+6);
            int key = hashFunction(substring);
            if(table.containsKey(key)){
                AVLNode temp = table.get(key).search(substring);
                if(temp == AVLTree.NIL) table.get(key).insert(substring, lineCount, j+1);
                else{
                    temp.locList.add(new pairInt(lineCount, j+1));
                }
            }else{
                AVLTree newTree = new AVLTree();
                newTree.insert(substring, lineCount, j+1);
                table.put(key, newTree);
            }
        }
        System.out.println(lineCount);
    }

    private static int hashFunction(String substring){
        int num = 0;
        for(int i=0; i<substring.length(); i++){
            num += (int)substring.charAt(i);
        }
        return num%100;
    }
}
class LinkedList<T> {
    private Node<T> head;
    private int size;
    private static class Node<T> {
        private T data;
        private Node<T> next;
        public Node(T data) {
            this.data = data;
            this.next = null;
        }
    }
    public LinkedList() {
        this.head = null;
        this.size = 0;
    }
    public boolean isEmpty(){
        if(head == null)
            return true;
        else return false;
    }
    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }
    public void clear(){
        head = null;
        size = 0;
    }
    public ArrayList<T> copyList(){
        ArrayList<T> temp = new ArrayList<T>();
        Node<T> current = head;
        while(current != null){
            temp.add(current.data);
            current = current.next;
        }
        return temp;
    }
    public void remove(T data) {
        if (head == null) {
            return;
        }
        if (head.data.equals(data)) {
            head = head.next;
            size--;
            return;
        }
        Node<T> current = head;
        Node<T> previous = null;
        while (current != null && !current.data.equals(data)) {
            previous = current;
            current = current.next;
        }
        if (current != null) {
            previous.next = current.next;
            size--;
        }
    }
    public int size() {
        return size;
    }
    public void display() {
        Node<T> current = head;
        while (current != null) {
            System.out.print(current.data + " ");
            current = current.next;
        }
        System.out.println();
    }
}

class pairInt implements Comparable {
    public int i;
    public int j;

    public pairInt(int i, int j){
        this.i = i;
        this.j = j;
    }
    public boolean equals(pairInt element){
        if(this.i == element.i && this.j == element.j)
            return true;
        return false;
    }
    public pairInt copy(){
        return this;
    }

    @Override
    public int compareTo(Object o) {
        if(this.i == ((pairInt)o).i && this.j == ((pairInt)o).j)
            return 0;
        else return 1;
    }
}

class AVLNode{
    private String substring = null;
    public AVLNode left = null;
    public AVLNode right = null;
    public int height = 0;
    LinkedList<pairInt> locList = null;

    public AVLNode(String substring, LinkedList<pairInt> locList){
        this.locList = locList;
        this.substring = substring;
        this.height = 1;
        this.left = AVLTree.NIL;
        this.right = AVLTree.NIL;
    }

    public AVLNode(String substring, LinkedList<pairInt> locList, AVLNode left, AVLNode right, int height){
        this.locList = locList;
        this.substring = substring;
        this.height = height;
        this.left = left;
        this.right = right;
    }

    public String getSubstring() {
        return substring;
    }
    public void setSubstring(String substring) {
        this.substring = substring;
    }
    public void addLocPoint(int i, int j){
        pairInt temp = new pairInt(i, j);
        locList.add(temp);
    }
}

class AVLTree{
    private AVLNode root = null;
    static final AVLNode NIL = new AVLNode(null, null, null, null, 0);
    private final int LL = 1;
    private final int LR = 2;
    private final int RR = 3;
    private final int RL = 4;
    private final int NO_NEED = 0;
    private final int ILLEGAL = -1;
    public AVLTree(){
        root = NIL;
    }

    public boolean isEmpty(){
        if(root == NIL) return true;
        else return false;
    }

    public void print(){
        if(root == NIL) System.out.println("Tree is Empty! Warning!");
        else{
            System.out.print(root.getSubstring());
            printTree(root.left);
            printTree(root.right);
        }
        System.out.println();
    }
    public void printTree(AVLNode tNode){
        if(tNode == NIL) return;
        System.out.print(" "+tNode.getSubstring());
        printTree(tNode.left);
        printTree(tNode.right);
    }

    public AVLNode search(String substring){
        return searchItem(root, substring);
    }
    private AVLNode searchItem(AVLNode tNode, String substring){
        if(tNode == NIL) return NIL;
        else if(substring.compareTo(tNode.getSubstring())==0) return tNode;
        else if(substring.compareTo(tNode.getSubstring())<0) return searchItem(tNode.left, substring);
        else return searchItem(tNode.right, substring);
    }

    public void insert(String substring, int i, int j){
        LinkedList<pairInt> temp = new LinkedList<pairInt>();
        temp.add(new pairInt(i, j));
        root = insertItem(root, substring, temp);
    }
    private AVLNode insertItem(AVLNode tNode, String substring, LinkedList<pairInt> temp){
        int type = ILLEGAL;
        if(tNode == NIL){
            tNode = new AVLNode(substring, temp);
        }else if(substring.compareTo(tNode.getSubstring())<0){
            tNode.left = insertItem(tNode.left, substring, temp);
            tNode.height = 1 + Math.max(tNode.right.height, tNode.left.height);
            type = needBalance(tNode);
            if(type != NO_NEED) tNode = balanceAVL(tNode, type);
        }else{
            tNode.right = insertItem(tNode.right, substring, temp);
            tNode.height = 1 + Math.max(tNode.right.height, tNode.left.height);
            type = needBalance(tNode);
            if(type != NO_NEED) tNode = balanceAVL(tNode, type);
        }
        return tNode;
    }

    public void delete(String substring){
        root = findAndDelete(root, substring);
    }
    private AVLNode findAndDelete(AVLNode tNode, String substring){
        if(tNode == NIL) return NIL;
        else{
            if(substring.compareTo(tNode.getSubstring())==0) tNode = deleteNode(tNode);
            else if(substring.compareTo(tNode.getSubstring())<0){
                tNode.left = findAndDelete(tNode.left, substring);
                tNode.height = 1 + Math.max(tNode.right.height, tNode.left.height);
                int type = needBalance(tNode);
                if(type != NO_NEED) tNode = balanceAVL(tNode, type);
            }else{
                tNode.right = findAndDelete(tNode.right, substring);
                tNode.height = 1 + Math.max(tNode.right.height, tNode.left.height);
                int type = needBalance(tNode);
                if(type != NO_NEED) tNode = balanceAVL(tNode, type);
            }
            return tNode;
        }
    }
    private AVLNode deleteNode(AVLNode tNode){
        if(tNode.left == NIL&&tNode.right == NIL) return NIL;
        else if(tNode.left == NIL) return tNode.right;
        else if(tNode.right == NIL) return tNode.left;
        else{
            returnPair rPair = deleteMinItem(tNode.right);
            tNode.setSubstring(rPair.str);
            tNode.locList = rPair.locList;
            tNode.right = rPair.node;
            tNode.height = 1 + Math.max(tNode.right.height, tNode.left.height);
            int type = needBalance(tNode);
            if(type != NO_NEED) tNode = balanceAVL(tNode, type);
            return tNode;
        }
    }
    private returnPair deleteMinItem(AVLNode tNode){
        if(tNode.left == NIL) return new returnPair(tNode.getSubstring(), tNode.locList, tNode.right);
        else{
            returnPair rPair = deleteMinItem(tNode.left);
            tNode.left = rPair.node;
            tNode.height = 1 + Math.max(tNode.right.height, tNode.left.height);
            int type = needBalance(tNode);
            if(type != NO_NEED) tNode = balanceAVL(tNode, type);
            rPair.node = tNode;
            return rPair;
        }
    }
    private class returnPair{
        String str = null;
        LinkedList<pairInt> locList = null;
        AVLNode node = null;
        private returnPair(String str, LinkedList<pairInt> locList, AVLNode node){
            this.str = str;
            this.locList = locList;
            this.node = node;
        }
    }
    private int needBalance(AVLNode t){
        int type = ILLEGAL;
        if(t.left.height+2<=t.right.height){
            if(t.right.left.height<=t.right.right.height) type = RR;
            else type = RL;
        }else if(t.left.height >= t.right.height+2){
            if(t.left.left.height >= t.left.right.height) type = LL;
            else type = LR;
        }else type = NO_NEED;
        return type;
    }
    private AVLNode balanceAVL(AVLNode tNode, int type){
        AVLNode returnNode = NIL;
        switch (type){
            case LL:
                returnNode = rightRotate(tNode);
                break;
            case LR:
                tNode.left = leftRotate(tNode.left);
                returnNode = rightRotate(tNode);
                break;
            case RR:
                returnNode = leftRotate(tNode);
                break;
            case RL:
                tNode.right = rightRotate(tNode.right);
                returnNode = leftRotate(tNode);
                break;
        }
        return returnNode;
    }
    private AVLNode leftRotate(AVLNode t){
        AVLNode RChild = t.right;
        if(RChild == NIL) System.out.println("t's RChild shouldn't be NIL.");
        AVLNode RLChild = RChild.left;
        RChild.left = t;
        t.right = RLChild;
        t.height = 1 + Math.max(t.left.height, t.right.height);
        RChild.height = 1 + Math.max(RChild.left.height, RChild.right.height);
        return RChild;
    }
    private AVLNode rightRotate(AVLNode t){
        AVLNode LChild = t.left;
        if(LChild == NIL) System.out.println("t's LChild shouldn't be NIL.");
        AVLNode LRChild = LChild.right;
        LChild.right = t;
        t.left = LRChild;
        t.height = 1 + Math.max(t.left.height, t.right.height);
        LChild.height = 1 + Math.max(LChild.left.height, LChild.right.height);
        return LChild;
    }
}
