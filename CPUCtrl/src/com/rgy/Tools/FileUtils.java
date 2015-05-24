package com.rgy.Tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import android.content.Context;
import android.os.Environment;

public class FileUtils {
	
	/**
	 * ��appListд���ļ�
	 * @param context
	 * @param appList
	 * @param fileName
	 * @return
	 */
	public static boolean writeListToFile(ArrayList<String> appList,String fileName){
		try {
			FileOutputStream fos = new FileOutputStream(fileName);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(appList);
			fos.close();
			oos.close();
			return true;
		} catch (Exception e) {
			//e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * ���ļ��е�appList��ȡ����
	 * @param context
	 * @param fileName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<String> readListFromFile(String fileName){
		try{
			FileInputStream fis = new FileInputStream(fileName);
			ObjectInputStream ois = new ObjectInputStream(fis);
			ArrayList<String> appList = (ArrayList<String>)ois.readObject();
			fis.close();
			ois.close();
			return appList;
		}catch(Exception e){
			//e.printStackTrace();
		}
		return null;
	}
	
	///////////////////// 
	
	public static void writeToFile(String fileName, String conent) {
		String dir = getSDPath()+"/mydata";   
        File file = new File(dir, fileName); 
		try {
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file, true)));
			out.write(conent);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String readFromFile(String fileName) {
		try {
			String dir = getSDPath()+"/mydata";   
	        File file = new File(dir, fileName);   
	        //
			FileInputStream is = new FileInputStream(file);
			byte[] b = new byte[is.available()];
			is.read(b);
			is.close();
			String result = new String(b);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // �ж�sd���Ƿ����
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// ��ȡ��Ŀ¼
		}
		return sdDir.toString();
	}
	
	public static void makeDir(){
        String path= getSDPath()+"/acon";   
        File dir=new File(path);   
        if(!dir.exists()){
        	dir.mkdir();
        	System.out.println("acon����Ŀ¼�ѽ���");
        }
	}
	
	public static void createFile(){
		String dir = getSDPath()+"/acon";   
        File file1 = new File(dir, "app_default.rgy");   
        File file2 = new File(dir, "app_performance.rgy");
        File file3 = new File(dir, "app_powersave.rgy");
        
        if(!file1.exists()){
        	try {
        		file1.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        //
        if(!file2.exists()){
        	try {
        		file2.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        //
        if(!file3.exists()){
        	try {
        		file3.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }

	}
	
	/**
	 * app�����б�������
	 * @param context
	 * @param model
	 * @return
	 */
	public static ArrayList<String> getAppClassifyList(Context context, String model){
		try {
			InputStream in = context.getResources().getAssets().open("smart.xml");
			SAXReader sr = new SAXReader();// ��ȡ��ȡxml�Ķ���
		    Document document = sr.read(in);
		    Element root = document.getRootElement();
			Element performance = root.element(model) ;//�õ���Ԫ��submit 
			List<Element> list = performance.elements();//�ĵ�submit��������Ԫ��
			ArrayList<String> myList = new ArrayList<String>();
			for(Element e : list){
				myList.add(e.getText());
			}
			return myList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}







