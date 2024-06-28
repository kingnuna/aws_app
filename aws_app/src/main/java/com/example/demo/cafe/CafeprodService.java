package com.example.demo.cafe;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.store.Store;

@Service
public class CafeprodService {
	@Autowired
	private CafeprodDao dao;
	
	public CafeprodDto save(CafeprodDto dto) {
		Cafeprod cp = dao.save(new Cafeprod(dto.getNum(), dto.getName(), dto.getPrice(), dto.getStore(), 
				dto.getFname()));
		return new CafeprodDto(cp.getNum(), cp.getName(), cp.getPrice(), cp.getStore(), cp.getFname(), null);
	}
	
	
	public CafeprodDto getProd(int num) {
		Cafeprod cp = dao.findById(num).orElse(null);
		if(cp == null) {
			return null;
		}
		return new CafeprodDto(cp.getNum(), cp.getName(), cp.getPrice(), cp.getStore(), cp.getFname(), null);
	}
	
	public ArrayList<CafeprodDto> getbyStore(String store){
		System.out.println("service store:"+store);
		List<Cafeprod> l = dao.findByStore(new Store(store, null, "", false));
		ArrayList<CafeprodDto> list = new ArrayList<CafeprodDto>();
		for(Cafeprod cp:l) {
			list.add(new CafeprodDto(cp.getNum(), cp.getName(), cp.getPrice(), cp.getStore(), cp.getFname(), null));
		}
		return list;
	}
	
	public void delProd(int num) {
		dao.deleteById(num);
	}
}
