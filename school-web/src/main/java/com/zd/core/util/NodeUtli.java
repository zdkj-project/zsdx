package com.zd.core.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuyubin
 *
 * @param <T>
 */
public class NodeUtli<T> {
	private T currentNode;// 本级node值
	private NodeUtli<T> parent;// 父级node
	private String value = "001"; // 本级标识值
	private List<NodeUtli<T>> nodeList = new ArrayList<>();
	
	public NodeUtli(){}
	/**
	 * 
	 * @param node 本级node值
	 * @param parentNode 父级node
	 * @param value 本级编号
	 */
	public NodeUtli(T node,NodeUtli<T> parentNode,String value){
		this.currentNode = node;
		this.parent = parentNode;
		this.value = value;
	}
	
	public T getCurrentNode() {
		return currentNode;
	}
	public void setCurrentNode(T currentNode) {
		this.currentNode = currentNode;
	}
	public NodeUtli<T> getParent() {
		return parent;
	}
	public void setParent(NodeUtli<T> parent) {
		this.parent = parent;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * 获取子级集合
	 * @return
	 */
	public List<NodeUtli<T>> getNodeList() {
		return nodeList;
	}
	/*public void setNodeList(List<NodeUtli<T>> nodeList) {
		this.nodeList = nodeList;
	}*/
	
}
