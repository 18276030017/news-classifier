package NewsClassify;

/**
 * ����ʵ����
 * 
 * @author lyq
 * 
 */
public class Word implements Comparable<Word> {
	// ��������
	String name;
	// ��Ƶ
	Integer count;

	public Word(String name, Integer count) {
		this.name = name;
		this.count = count;
	}

	@Override
	public int compareTo(Word o) {
		// TODO Auto-generated method stub
		return o.count.compareTo(this.count);
	}
}
