package com.qq.client;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * �Զ����ԴList
 * ��������һ��List���ⲿͨ��setSources(List sources)������Դ
 * BaseList Demo ������
 * @ClassName ListSource
 * @author Jet
 * @date 2012-8-7
 */
public class ListSource extends BaseModel{
	private Comparator<Object> comparator=null;
	private List<Object> sources=new ArrayList<Object>();
	//ͨ��list��������Դ
	public void setSources(List<Object> sources) {
		this.sources = sources;
	}
	//���һ����Ԫ
	public void addCell(Object obj){
		sources.add(obj);
		notifySourceRefreshEvent(sources);
	}
	//��������ɾ��һ����Ԫ
	public void removeCell(int index){
		sources.remove(index);
		notifySourceRefreshEvent(sources);
	}
	//����ֵɾ��һ����Ԫ
	public void removeCell(Object value){
		sources.remove(value);
		notifySourceRefreshEvent(sources);
	}
	//����һ����Ԫ
	public void setCell(int index,Object obj){
		sources.set(index, obj);
		notifySourceRefreshEvent(sources);
	}
	//��ȡһ����Ԫ����Ϣ
	public Object getCell(int index){
		return sources.get(index);
	}
	//��ȡ���е�Ԫ��Ϣ
	public List<Object> getAllCell(){
		return sources;
	}
	//�Ƴ�����
	public void removeAll(){
		sources.clear();
		notifySourceRefreshEvent(sources);
	}
	public void setSort(Comparator<Object> comparator){
		this.comparator=comparator;
		notifySourceRefreshEvent(sources);
	}
	public Comparator<Object> getComparator() {
		return comparator;
	}
}
