package NewsClassify;

import java.util.ArrayList;


/**
 * ���ŷ����㷨������
 * @author lyq
 *
 */
public class Client {
	public static void main(String[] args){
		String testFilePath1;
		String testFilePath2;
		String testFilePath3;
		String path;
		String newsType;
		int vectorNum;
		double minSupportValue;
		ArrayList<String> trainDataPaths;
		NewsClassifyTool classifyTool;
		
		//��Ӳ����Լ�ѵ���������ļ�·��
		testFilePath1 = "C:\\Users\\lyq\\Desktop\\icon\\test\\testNews1.txt";
		testFilePath2 = "C:\\Users\\lyq\\Desktop\\icon\\test\\testNews2.txt";
		testFilePath3 = "C:\\Users\\lyq\\Desktop\\icon\\test\\testNews3.txt";
		trainDataPaths = new ArrayList<String>();
		path = "C:\\Users\\lyq\\Desktop\\icon\\test\\trainNews1.txt";
		trainDataPaths.add(path);
		path = "C:\\Users\\lyq\\Desktop\\icon\\test\\trainNews2.txt";
		trainDataPaths.add(path);
		
		newsType = "����";
		vectorNum = 10;
		minSupportValue = 0.45;
		
		classifyTool = new NewsClassifyTool(trainDataPaths, newsType, vectorNum, minSupportValue);
		classifyTool.newsClassify(testFilePath1);
		classifyTool.newsClassify(testFilePath2);
		classifyTool.newsClassify(testFilePath3);
	}

}
