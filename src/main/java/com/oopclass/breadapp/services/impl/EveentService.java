package com.oopclass.breadapp.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oopclass.breadapp.models.Eveent;
import com.oopclass.breadapp.repository.EveentRepository;
import com.oopclass.breadapp.services.IEveentService;

@Service
public class EveentService implements IEveentService {
	
	@Autowired
	private EveentRepository eveentRepository;
	
	@Override
	public Eveent save(Eveent entity) {
		return eveentRepository.save(entity);
	}

	@Override
	public Eveent update(Eveent entity) {
		return eveentRepository.save(entity);
	}

	@Override
	public void delete(Eveent entity) {
		eveentRepository.delete(entity);
	}

	@Override
	public void delete(Long id) {
		eveentRepository.deleteById(id);
	}

	@Override
	public Eveent find(Long id) {
		return eveentRepository.findById(id).orElse(null);
	}

	@Override
	public List<Eveent> findAll() {
		return eveentRepository.findAll();
	}

	@Override
	public void deleteInBatch(List<Eveent> eveents) {
		eveentRepository.deleteInBatch(eveents);
	}
	
}
