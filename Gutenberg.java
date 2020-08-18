
import java.io.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;


public class Gutenberg{
	
	public String getText(File file) throws FileNotFoundException {
		StringBuilder text = new StringBuilder();
		Scanner scan = new Scanner(file);
		while(scan.hasNextLine()) {
			text.append(scan.nextLine() + " ");
		}
		scan.close();
		return text.toString();
	}
	public int getTotalNumberOfWords(File file) throws FileNotFoundException {
		String text = getText(file);
		
		String[] words = text.toString().split(" ");
		int numberOfWords = 0;
		for(int i = 0; i < words.length;i++) {
			if(!words[i].equals("")) {
				numberOfWords++;
			}
		}
		return numberOfWords;
	}
	public int getTotalUniqueWords(File file) throws FileNotFoundException {
		String text = getText(file);
		String[] words = text.toString().split(" ");
		Map<String,Integer> map = new HashMap<>();
		int size = 0;
		for(int i = 0; i < words.length;i++) {
			if(words[i].equals(""))continue;
			map.put(words[i],map.getOrDefault(words[i], 0)+1);
		}
		for(Map.Entry<String, Integer> data : map.entrySet()) {
			if(data.getValue()== 1) {
				size++;
			}
		}
		return size;
	}
	
	public	Object[][] get20MostFrequentWords(File file) throws FileNotFoundException {
		Object[][] result = new Object[20][2];
		String text = getText(file);
		String[] words = text.split(" ");
		
		Map<String,Integer> map = new HashMap<>();
		for(int i = 0; i < words.length;i++) {
			if(words[i].equals(""))continue;
			map.put(words[i],map.getOrDefault(words[i], 0)+1);
		}
		
		PriorityQueue<String> pq = new PriorityQueue<>(new Comparator<String>(){
			@Override
			public int compare(String a, String b) {
				return map.get(b)-map.get(a);
			}
		});
		pq.addAll(map.keySet());
		int i = 0;
		while(!pq.isEmpty() && i < 20) {
			Object[] top = new Object[2];
			String curr = pq.poll();
			top[0] = curr;
			top[1] = map.get(curr);
			result[i]= top;
			i++;
		}
		return result;
	}
	
	public Object[][] get20MostInterestingFrequentWords(File file, File commonWords) throws FileNotFoundException {
		String text = getText(file);
		String badWords = getText(commonWords);
		Object[][] result = new Object[20][2];	
		Set<String> set = new HashSet<>();
		String[] removeWords = badWords.split(" ");
		for(String s: removeWords) {
			if(s.equals(""))continue;
			set.add(s);
		}
		String[] words = text.split(" ");
		Map<String,Integer> map = new HashMap<>();
		for(int i = 0; i < words.length;i++) {
			if(words[i].equals("") || set.contains(words[i]))continue;
			map.put(words[i],map.getOrDefault(words[i], 0)+1);
		}
		
		PriorityQueue<String> pq = new PriorityQueue<>(new Comparator<String>(){
			@Override
			public int compare(String a, String b) {
				return map.get(b)-map.get(a);
			}
		});
		pq.addAll(map.keySet());
		int i = 0;
		while(!pq.isEmpty() && i < 20) {
			Object[] top = new Object[2];
			String curr = pq.poll();
			top[0] = curr;
			top[1] = map.get(curr);
			result[i] = top;
			i++;
		}
		return result;
		
	}
	
	public Object[][] get20LeastFrequentWords(File file) throws FileNotFoundException{
		Object[][] result = new Object[20][2];
		String text = getText(file);
		String[] words = text.split(" ");
		
		Map<String,Integer> map = new HashMap<>();
		for(int i = 0; i < words.length;i++) {
			if(words[i].equals(""))continue;
			map.put(words[i],map.getOrDefault(words[i], 0)+1);
		}
		
		PriorityQueue<String> pq = new PriorityQueue<>(new Comparator<String>(){
			@Override
			public int compare(String a, String b) {
				return map.get(a)-map.get(b);
			}
		});
		pq.addAll(map.keySet());
		int i = 0;
		while(!pq.isEmpty() && i < 20) {
			Object[] top = new Object[2];
			String curr = pq.poll();
			top[0] = curr;
			top[1] = map.get(curr);
			result[i]= top;
			i++;
		}
		return result;
	}
	
	public int numberOfChapters(File file) throws FileNotFoundException {
		String[] words = getText(file).split(" ");
		int count = 0;
		for(int i = 0; i < words.length;i++) {
			if(words[i].equals("CHAPTER")) {
				count++;
			}
		}
		return count;
	}
	public int[] getFrequencyOfWord(String word,File file) throws FileNotFoundException {
		int chapters = numberOfChapters(file);
		int[] counts = new int[chapters];
		String text = getText(file);
		String[] words = text.split(" ");
		
		int i = -1;
		int count = 0;
		for(int j = 0; j < words.length;j++) {	
			if(words[j].equals("")) continue;
			if(words[j].equals("CHAPTER")){
				i++;
				if(i==0)continue;
				counts[i-1] = count;
				count = 0;
			}
			String curr = newWord(words[j]);
			if(curr.toLowerCase().equals(word.toLowerCase()))count++;
		}
		counts[i] = count;
		
		return counts;
	}
	public String newWord(String curr) {
		StringBuilder str = new StringBuilder();
		for(int i = 0; i < curr.length();i++){
			if(Character.isAlphabetic(curr.charAt(i))) {
				str.append(curr.charAt(i));
			}
		}
		return str.toString();
	}
	public void setChapters(String book,String[] chapters) {
		String[] words = book.split(" ");
		StringBuilder str = new StringBuilder();
		int index = -1;
		for(int i = 0; i < words.length;i++) {
			if(words[i].equals(""))continue;
			if(words[i].equals("CHAPTER")) {
				index++;
				if(index==0)continue;
				chapters[index] = str.toString();
				str = new StringBuilder();
			}
			str.append(words[i]);
		}
		chapters[index+1] = str.toString();
		
	}
	public int getChapterQuoteAppears(String quote,File file) throws FileNotFoundException {
		String txt = getText(file);
		int size = numberOfChapters(file);
		String[] chapters = new String[size+1];
		setChapters(txt,chapters);
		String newQuote = findQuote(quote);
		for(int i = 1; i < chapters.length;i++) {
			String chap = chapters[i];
			int length = chap.length()-newQuote.length();
			for(int j = 0; j <= length;j++) {
				String guess = chap.substring(j,newQuote.length() + j);
				if(guess.equals(newQuote))return i;
			}
		}
		return -1;
	}
	public String findQuote(String curr) {
		StringBuilder str = new StringBuilder();
		for(int i = 0; i < curr.length();i++) {
			if(curr.charAt(i)!=' ') {
				str.append(curr.charAt(i));
			}
		}
		return str.toString();
	}
	public static void main(String[] args) throws FileNotFoundException {
		File file = new File("/Users/michaelromero/Desktop/JavaProjects/521-0.txt"); 
		File commonWords = new File("/Users/michaelromero/Desktop/JavaProjects/1-100.txt");
		Gutenberg project = new Gutenberg();
		System.out.println(project.getChapterQuoteAppears("but I thought I perceived them just going to fly from me, when I spoke to them in English. “Gentlemen,” ",file));
		//System.out.println(project.getTotalNumberOfWords(file));
		//System.out.println(project.getTotalUniqueWords(file));
		//Object[][] result = project.get20MostFrequentWords(file);
		/**
		 * 
		 
		for(int i = 0; i < result.length;i++) {
			String word = (String)result[i][0];
			int count = (int)result[i][1];
			System.out.println(word + " " + count);
		}
		*/
		//System.out.println();
		/**
		 * 
		 
	     Object[][] result2 = project.get20MostInterestingFrequentWords(file,commonWords);	
		for(int i = 0; i < result2.length;i++) {
			String word = (String)result2[i][0];
			int count = (int)result2[i][1];
			System.out.println(word + " " + count);
		}
		*/
		/**
		 * 
		
		Object[][] result3 = project.get20LeastFrequentWords(file);
		for(int i = 0; i < result3.length;i++) {
			String word = (String)result3[i][0];
			int count = (int)result3[i][1];
			System.out.println(word + " " + count);
		}
		 */
		
		//System.out.println(project.numberOfChapters(file));
		//int[] counts = project.getFrequencyOfWord("being",file);
		
	}
}
