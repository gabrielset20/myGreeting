package ifam.edu.dra.chat.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ifam.edu.dra.chat.model.Contato;
import ifam.edu.dra.chat.repositories.ContatoRepository;

@Service
public class ContatoService {
	
	@Autowired
	ContatoRepository contatoRepository;
	
	public List<Contato> getContatos(){
		return contatoRepository.findAll();
	}
	
	public Contato getContato(Long id) {
		Optional<Contato> optionalContato = contatoRepository.findById(id);
		if(optionalContato.isPresent())
			return optionalContato.get();
		return new Contato();
	}
	
	public Contato setContato(Contato contato) {
		return contatoRepository.save(contato);
	}
	
	public Contato setContato(Long id, Contato contato) {
		Optional<Contato> optionalContato = contatoRepository.findById(id);
		if(optionalContato.isPresent()) {
			contato.setId(id);
			return contatoRepository.save(contato);
		}
		return new Contato();
	}
	
	public void deleteContato(Long id) {
		Optional<Contato> optionalContato = contatoRepository.findById(id);
		if(optionalContato.isPresent()) {
			contatoRepository.deleteById(id);
		}
	}
}