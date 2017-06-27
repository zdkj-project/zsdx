package com.zd.core.util;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 用于List对象排序。
 * @author huangzc
 * @param <E>
 */
public class SortListUtil<E> {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public  void  Sort(List<E> list, final String obj, final String sort) {
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				int ret = 0;
				try {
					Field fiedl[] = o1.getClass().getDeclaredFields();
					Field f = null;
					for(Field typeF : fiedl){
						typeF.setAccessible(true);
						if(typeF.getName().equals(obj)){
							f = typeF;
							break;
						}
					}
					if(f == null)
						return 0;
					Class<?> m1T = f.getType();
					if(sort == null || sort.trim().equals("")){
						if (m1T == int.class) {
							ret = ((Integer) f.getInt(o1)).compareTo((Integer) f
									.getInt(o2));
						} else if (m1T == double.class) {
							ret = ((Double) f.getDouble(o1)).compareTo((Double) f
									.getDouble(o2));
						} else if (m1T == long.class) {
							ret = ((Long) f.getLong(o1)).compareTo((Long) f
									.getLong(o2));
						} else if (m1T == float.class) {
							ret = ((Float) f.getFloat(o1)).compareTo((Float) f
									.getFloat(o2));
						} else if (m1T == Date.class) {
							ret = ((Date) f.get(o1)).compareTo((Date) f.get(o2));
						} else if (isImplementsOf(m1T, Comparable.class)) {
							ret = ((Comparable) f.get(o1)).compareTo((Comparable) f
									.get(o2));
						} else {
							ret = String.valueOf(f.get(o1)).compareTo(
									String.valueOf(f.get(o2)));
						}
					}else {
						if (m1T == int.class) {
							ret = ((Integer) f.getInt(o2)).compareTo((Integer) f
									.getInt(o1));
						} else if (m1T == double.class) {
							ret = ((Double) f.getDouble(o2)).compareTo((Double) f
									.getDouble(o1));
						} else if (m1T == long.class) {
							ret = ((Long) f.getLong(o2)).compareTo((Long) f
									.getLong(o1));
						} else if (m1T == float.class) {
							ret = ((Float) f.getFloat(o2)).compareTo((Float) f
									.getFloat(o1));
						} else if (m1T == Date.class) {
							ret = ((Date) f.get(o2)).compareTo((Date) f.get(o1));
						} else if (isImplementsOf(m1T, Comparable.class)) {
							ret = ((Comparable) f.get(o2)).compareTo((Comparable) f
									.get(o1));
						} else {
							ret = String.valueOf(f.get(o2)).compareTo(
									String.valueOf(f.get(o1)));
						}
					}
				} catch (SecurityException e) {
					System.out.println(e);
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					System.err.println(e);
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				return ret;
			}
		});
	}
	
	/**
	 * 
	 * 
	 * @param clazz
	 * @param szInterface
	 * @return
	 */
	public static boolean isImplementsOf(Class<?> clazz, Class<?> szInterface) {
		boolean flag = false;

		Class<?>[] face = clazz.getInterfaces();
		for (Class<?> c : face) {
			if (c == szInterface) {
				flag = true;
			} else {
				flag = isImplementsOf(c, szInterface);
			}
		}
		if (!flag && null != clazz.getSuperclass()) {
			return isImplementsOf(clazz.getSuperclass(), szInterface);
		}

		return flag;
	}
}
