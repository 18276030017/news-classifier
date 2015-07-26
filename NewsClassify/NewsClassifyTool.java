package NewsClassify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import ICTCLAS.I3S.AC.ICTCLAS50;

/**
 * �����㷨ģ��
 * 
 * @author lyq
 * 
 */
public class NewsClassifyTool {
	// ���������ռ�ά��
	private int vectorNum;
	// �������ƶ���С������ֵ
	private double minSupportValue;
	// ��ǰѵ�����ݵ��������
	private String newsType;
	// ѵ�����������ļ���ַ
	private ArrayList<String> trainDataPaths;

	public NewsClassifyTool(ArrayList<String> trainDataPaths, String newsType,
			int vectorNum, double minSupportValue) {
		this.trainDataPaths = trainDataPaths;
		this.newsType = newsType;
		this.vectorNum = vectorNum;
		this.minSupportValue = minSupportValue;
	}

	/**
	 * ���ļ��ж�ȡ����
	 */
	private String readDataFile(String filePath) {
		File file = new File(filePath);
		StringBuilder strBuilder = null;

		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String str;
			strBuilder = new StringBuilder();
			while ((str = in.readLine()) != null) {
				strBuilder.append(str);
			}
			in.close();
		} catch (IOException e) {
			e.getStackTrace();
		}

		return strBuilder.toString();
	}

	/**
	 * ����������ݵ���������
	 */
	private double[] calCharacterVectors(String filePath) {
		int index;
		double[] vectorDimensions;
		double[] temp;
		News news;
		News testNews;
		String newsCotent;
		String testContent;
		String parseContent;
		// ��Ƶ�ʻ�
		ArrayList<Word> frequentWords;
		ArrayList<Word> wordList;

		testContent = readDataFile(filePath);
		testNews = new News(testContent);
		parseNewsContent(filePath);

		index = filePath.indexOf('.');
		parseContent = readDataFile(filePath.substring(0, index) + "-split.txt");
		testNews.statWords(parseContent);

		vectorDimensions = new double[vectorNum];
		// ����ѵ�����ݼ���������������
		for (String path : this.trainDataPaths) {
			newsCotent = readDataFile(path);
			news = new News(newsCotent);

			// ���зִʲ���
			index = path.indexOf('.');
			parseNewsContent(path);
			parseContent = readDataFile(path.substring(0, index) + "-split.txt");
			news.statWords(parseContent);

			wordList = news.wordDatas;
			// ����Ƶͳ�ƽ����������
			Collections.sort(wordList);

			frequentWords = new ArrayList<Word>();
			// ��ȡ��ǰvectorDimens�Ĵ���
			for (int i = 0; i < vectorNum; i++) {
				frequentWords.add(wordList.get(i));
			}

			temp = testNews.calVectorDimension(frequentWords);
			// ����������ֵ�����ۼ�
			for (int i = 0; i < vectorDimensions.length; i++) {
				vectorDimensions[i] += temp[i];
			}
		}

		// ���ȡƽ������ֵ��Ϊ���յ���������ֵ
		for (int i = 0; i < vectorDimensions.length; i++) {
			vectorDimensions[i] /= trainDataPaths.size();
		}

		return vectorDimensions;
	}

	/**
	 * ������õ������ռ�����������ƶ�ֵ
	 * 
	 * @param vectorDimension
	 *            ����õĲ������ݵ���������ֵ
	 * @return
	 */
	private double calCosValue(double[] vectorDimension) {
		double result;
		double num1;
		double num2;
		double temp1;
		double temp2;
		// ��׼������������ÿ��ά���϶�Ϊ1
		double[] standardVector;

		standardVector = new double[vectorNum];
		for (int i = 0; i < vectorNum; i++) {
			standardVector[i] = 1;
		}

		temp1 = 0;
		temp2 = 0;
		num1 = 0;

		for (int i = 0; i < vectorNum; i++) {
			// �ۼӷ��ӵ�ֵ
			num1 += vectorDimension[i] * standardVector[i];

			// �ۼӷ�ĸ��ֵ
			temp1 += vectorDimension[i] * vectorDimension[i];
			temp2 += standardVector[i] * standardVector[i];
		}

		num2 = Math.sqrt(temp1) * Math.sqrt(temp2);
		// �������Ҷ���ʽ���м���
		result = num1 / num2;

		return result;
	}

	/**
	 * �������ŷ���
	 * 
	 * @param filePath
	 *            �������������ļ���ַ
	 */
	public void newsClassify(String filePath) {
		double result;
		double[] vectorDimension;

		vectorDimension = calCharacterVectors(filePath);
		result = calCosValue(vectorDimension);

		// ����������ƶ�ֵ������С��ֵҪ��������Ŀ�����
		if (result >= minSupportValue) {
			System.out.println(String.format("�������ƶȽ��Ϊ%s,������ֵ%s,���Դ���������%s������",
					result, minSupportValue, newsType));
		} else {
			System.out.println(String.format("�������ƶȽ��Ϊ%s,С����ֵ%s,���Դ����Ų�����%s������",
					result, minSupportValue, newsType));
		}
	}

	/**
	 * ���÷ִ�ϵͳ�����������ݵķִ�
	 * 
	 * @param srcPath
	 *            �����ļ�·��
	 */
	private void parseNewsContent(String srcPath) {
		// TODO Auto-generated method stub
		int index;
		String dirApi;
		String desPath;

		dirApi = System.getProperty("user.dir") + "\\lib";
		// ��װ���·��ֵ
		index = srcPath.indexOf('.');
		desPath = srcPath.substring(0, index) + "-split.txt";

		try {
			ICTCLAS50 testICTCLAS50 = new ICTCLAS50();
			// �ִ�������·������ʼ��
			if (testICTCLAS50.ICTCLAS_Init(dirApi.getBytes("GB2312")) == false) {
				System.out.println("Init Fail!");
				return;
			}
			// ���ļ���string����תΪbyte����
			byte[] Inputfilenameb = srcPath.getBytes();

			// �ִʴ��������ļ��������ļ���string����תΪbyte����
			byte[] Outputfilenameb = desPath.getBytes();

			// �ļ��ִ�(��һ������Ϊ�����ļ�����,�ڶ�������Ϊ�ļ���������,����������Ϊ�Ƿ��Ǵ��Լ�1 yes,0
			// no,���ĸ�����Ϊ����ļ���)
			testICTCLAS50.ICTCLAS_FileProcess(Inputfilenameb, 0, 1,
					Outputfilenameb);
			// �˳��ִ���
			testICTCLAS50.ICTCLAS_Exit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
