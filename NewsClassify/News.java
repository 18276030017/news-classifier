package NewsClassify;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * ����ʵ����
 * 
 * @author lyq
 * 
 */
public class News {
	// ���ž�������
	String content;
	// ���Ű����Ĵʵĸ���ͳ��ֵ
	HashMap<String, Integer> word2Count;
	// ���еĴʻ���Ϣ
	ArrayList<Word> wordDatas;

	public News(String content) {
		this.content = content;
		this.word2Count = new HashMap<String, Integer>();
		this.wordDatas = new ArrayList<Word>();
	}

	/**
	 * ���ִʺ���ַ������йؼ��ʴ���ͳ��
	 * 
	 * @param parseStr
	 */
	public void statWords(String parseStr) {
		int index;
		int count;
		String w;
		String[] array;
		Word word;

		array = parseStr.split(" ");
		for (String str : array) {
			index = str.indexOf('/');
			if(index == -1){
				continue;
			}
			w = str.substring(0, index);

			//ֻɸѡרҵ�Ե�����֮��Ĵ���ͱ����š�/wn
			if(!str.contains("n") || str.contains("wn")){
				continue;
			}
			
			count = 0;
			if (this.word2Count.containsKey(w)) {
				count = this.word2Count.get(w);
			}

			// �������ĸ���
			count++;
			this.word2Count.put(w, count);
		}

		// �����ܴ���ļ�¼����
		for (Map.Entry<String, Integer> entry : this.word2Count.entrySet()) {
			w = entry.getKey();
			count = entry.getValue();

			word = new Word(w, count);
			this.wordDatas.add(word);
		}
	}

	/**
	 * �������������ռ�ֵ
	 * 
	 * @param vectorWords
	 *            �����������Դ�
	 * @return
	 */
	public double[] calVectorDimension(ArrayList<Word> vectorWords) {
		String word;
		int count;
		int index;
		double value;
		double[] vectorDimension;

		//�ж��Ƿ��Ѿ�ͳ��
		vectorDimension = new double[vectorWords.size()];

		index = 0;
		// ����Ƚ�����������
		for (Word entry : vectorWords) {
			value = 0;
			count = 0;
			word = entry.name;

			if (this.word2Count.containsKey(word)) {
				count = this.word2Count.get(word);
			}

			// �����������ʵĳ���Ƶ����Ϊһ������ֵ
			value = 1.0 * count / entry.count;
			//����1����1����
			if(value > 1){
				value = 1;
			}
			
			vectorDimension[index] = value;
			index++;
		}

		return vectorDimension;
	}
}
