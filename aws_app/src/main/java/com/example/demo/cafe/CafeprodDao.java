package com.example.demo.cafe;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.store.Store;
@Repository
public interface CafeprodDao extends JpaRepository<Cafeprod, Integer> {
	ArrayList<Cafeprod> findByStore(Store store);
}
