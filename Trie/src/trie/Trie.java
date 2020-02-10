package trie;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }


	private static void insert(TrieNode check, int index, String[] allWords){

		if(allWords[check.substr.wordIndex].charAt(check.substr.startIndex)!=allWords[index].charAt(check.substr.startIndex)) {
			if(check.sibling==null){
				//insert as sibling
				Indexes currIndex = new Indexes(index,(short)0,(short)(allWords[index].length()-1));
				//creation of sibling
				check.sibling = new TrieNode(currIndex,null,null);
			}
			else{
				//call insert on sibling
				insert(check.sibling,index,allWords);
			}
		}
		else{
			int counter =0;
			boolean prefixMatch;
			int i =check.substr.startIndex;

			//if(the character of the new == the character of the node being checked
			while(i<=check.substr.endIndex&&allWords[index].charAt(i)==allWords[check.substr.wordIndex].charAt(i)){
				counter++; //index of last position matching +1
				i++;
			} // convert to while for repeating characters

			//check for if check is a prefix or not
			prefixMatch = (counter - 1 == (check.substr.endIndex-check.substr.startIndex));




			//if it is a prefix node fully matched
			if(prefixMatch){
				TrieNode newCurr;
				newCurr = check.firstChild;
				while(newCurr!=null){
					if(allWords[index].charAt(0)==allWords[newCurr.substr.wordIndex].charAt(0)){
						insert(newCurr,index,allWords);
						break;
					}
					else
						newCurr = newCurr.sibling;
				}
			}


			//if its not a prefix fully matched, is therefore partial prefix or word
			else{
				Indexes tempIndex = new Indexes(check.substr.wordIndex,check.substr.startIndex,check.substr.endIndex);
				TrieNode temp = new TrieNode(tempIndex,check.firstChild,check.sibling);

				check.substr.endIndex = (short)((counter-1)+check.substr.startIndex);
				TrieNode existing = new TrieNode(temp.substr,temp.firstChild,temp.sibling);
				existing.substr.startIndex = (short)(counter+check.substr.startIndex);

				if(check.firstChild!=null) {
					Indexes saveTemp = new Indexes(check.firstChild.substr.wordIndex, check.firstChild.substr.startIndex, check.firstChild.substr.endIndex);
					TrieNode save = new TrieNode(saveTemp, check.firstChild.firstChild, check.firstChild.sibling);
					existing.firstChild = save; //check if this pointer works
				}

				check.firstChild = existing;

				//if it has a child to reattach


				Indexes sibIndex = new Indexes(index,(short)(check.substr.endIndex+1),(short)(allWords[index].length()-1));
				//creation of sibling node
				existing.sibling = new TrieNode(sibIndex,null,null);

			}

			//to do:
			//if indexes to adjust
		}

	}
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {


		//root node child = first node

		TrieNode root = null;

		root = new TrieNode(null, null,null);
		//first node attached to nothing
		Indexes firstIndex = new Indexes(0,(short)0,(short)(allWords[0].length()-1));
		TrieNode firstNode = new TrieNode(firstIndex,null,null);

		root.firstChild=firstNode;

		//call insertion on all nodes
		if(allWords.length>1) {
			for (int i = 1; i < allWords.length; i++) {
				insert(firstNode, i, allWords);
			}
		}

		return root;
	}
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
													 String[] allWords, String prefix) {
		/* COMPLETE THIS METHOD */


		return find(root.firstChild,allWords, prefix);



		// FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
		// MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
	}

/*
((allWords[toCheck.substr.wordIndex].substring(toCheck.substr.startIndex, toCheck.substr.endIndex+1).contains(prefix))||
				((prefix).contains(allWords[toCheck.substr.wordIndex].substring(toCheck.substr.startIndex, toCheck.substr.endIndex+1))))
 */



	private static ArrayList<TrieNode> find(TrieNode toCheck,String[] allWords,String prefix) {

		if(toCheck == null) return null;

		int length = Math.min(allWords[toCheck.substr.wordIndex].length(), prefix.length());

		if (!(allWords[toCheck.substr.wordIndex].substring(0, length).contains(prefix.substring(0, length)))) {

				if (allWords[toCheck.substr.wordIndex].charAt(toCheck.substr.startIndex) != (prefix.charAt(toCheck.substr.startIndex))) { //add negatived

					if (toCheck.sibling != null)
						return find(toCheck.sibling, allWords, prefix);
					else
						return null;
				}
				else {
					return (find(toCheck.firstChild, allWords, prefix));
				}

			}


		ArrayList<TrieNode> answer = new ArrayList<TrieNode>();

		if(toCheck.firstChild==null){
			answer.add(toCheck);
		}
		else {
			arrCreateSibling(toCheck.firstChild, answer);
		}

		return answer;
	}


	private static ArrayList<TrieNode> arrCreateSibling(TrieNode operate, ArrayList<TrieNode> aggregate){

		if(operate.firstChild!=null)
			arrCreateSibling(operate.firstChild,aggregate);
		else
			aggregate.add(operate); // fix in case
		if(operate.sibling!=null){
			arrCreateSibling(operate.sibling,aggregate);
		}

		return aggregate;
	}



	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
