package ifam.edu.dra.chat.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ifam.edu.dra.chat.dto.MensagemDTO;
import ifam.edu.dra.chat.model.Contato;
import ifam.edu.dra.chat.model.Mensagem;
import ifam.edu.dra.chat.repositories.ContatoRepository;
import ifam.edu.dra.chat.repositories.MensagemRepository;
import ifam.edu.dra.chat.service.MensagemService;

@RestController
@RequestMapping
public class MensagemController {
	
	@Autowired
	MensagemService mensagemService;
	
	@Autowired
    private MensagemRepository mensagemRepository;

    @Autowired
    private ContatoRepository contatoRepository;

    @PostMapping("/mensagem")
    public ResponseEntity<?> criarMensagem(@RequestBody MensagemDTO mensagemDTO) {
        // Busque o Contato com base no emissor e receptor
        Optional<Contato> emissorOptional = contatoRepository.findById(mensagemDTO.getEmissor());
        Optional<Contato> receptorOptional = contatoRepository.findById(mensagemDTO.getReceptor());

        if (emissorOptional.isPresent() && receptorOptional.isPresent()) {
            Contato emissor = emissorOptional.get();
            Contato receptor = receptorOptional.get();

            // Crie uma instância de Mensagem a partir do DTO
            Mensagem mensagem = new Mensagem();
            mensagem.setId(mensagemDTO.getId());
            mensagem.setConteudo(mensagemDTO.getConteudo());
            mensagem.setEmissor(emissor);
            mensagem.setReceptor(receptor);

            // Salve a mensagem
            mensagemRepository.save(mensagem);

            return ResponseEntity.ok(mensagem);
        } else {
            return ResponseEntity.badRequest().body("Contato do emissor ou receptor não encontrado");
        }
    }


    // Outros métodos do controlador
    
    @GetMapping("/mensagem")
    ResponseEntity<List<MensagemDTO>> getMensagens() {
        List<Mensagem> mensagens = mensagemService.getMensagens();
        if (mensagens.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());

        // Transformar as mensagens em DTOs com IDs de emissores e receptores
        List<MensagemDTO> mensagemDTOs = mensagens.stream().map(mensagem -> {
            MensagemDTO mensagemDTO = new MensagemDTO();
            mensagemDTO.setId(mensagem.getId());
            mensagemDTO.setDataHora(mensagem.getDataHora());
            mensagemDTO.setConteudo(mensagem.getConteudo());
            mensagemDTO.setEmissor(mensagem.getEmissor().getId());
            mensagemDTO.setReceptor(mensagem.getReceptor().getId());
            return mensagemDTO;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(mensagemDTOs);
    }

	@GetMapping("/{id}")
	ResponseEntity<Mensagem> getMensagem(@PathVariable Long id) {
		try {
			return ResponseEntity.ok(mensagemService.getMensagem(id));	
		} catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensagem());
		}
	}

	@PutMapping("/{id}")
	 ResponseEntity<Mensagem> setMensagem(@RequestBody Mensagem mensagem, @PathVariable Long id) {
		try {
			return ResponseEntity.accepted().body(mensagemService.setMensagem(id, mensagem));	
		} catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensagem());
		}
	}
}
