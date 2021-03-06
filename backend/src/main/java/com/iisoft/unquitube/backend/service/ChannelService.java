package com.iisoft.unquitube.backend.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iisoft.unquitube.backend.dto.ChannelDTO;
import com.iisoft.unquitube.backend.dto.VideoDTO;
import com.iisoft.unquitube.backend.repository.ChannelRepository;

@Service
public class ChannelService {
	
	private ChannelRepository channelRepository;
	
	@Autowired
	public ChannelService(ChannelRepository channelRepository) {
		this.channelRepository = channelRepository;
	}

	@Transactional 
	public ChannelDTO addVideoToChannel(VideoDTO newVideo, Integer channelId) {		
		/* NOTA DE HIBERNATE: Todo pasa dentro de una transacción. 
		 * En este método no hace falta guardar el video explicitamente. 
		 * Cuando este método retorna Hibernate cierra la transacción y se encarga solo 
		 * de guardar el Channel con su nuevo video.
		 * */
		ChannelDTO foundedChannel = this.getChannel(channelId);
		
		foundedChannel.getPlaylist().add(newVideo);
		return foundedChannel;
	}

	public ChannelDTO getChannel(Integer channelId) {
		Optional<ChannelDTO> optionalChannel = this.channelRepository.findById(channelId);
		
		if (!optionalChannel.isPresent())
			return null;
		
		ChannelDTO foundedChannel = optionalChannel.get();
		foundedChannel.getPlaylist().size(); // hack para traer lista lazy
		return foundedChannel;
	}

	public ChannelDTO updateChannel(ChannelDTO channelDTO){
		this.channelRepository.save(channelDTO);
		return channelDTO;
	}

	public List<ChannelDTO> getAllChannels() {
		return this.channelRepository.findAll();
	}

	public ChannelDTO saveChannel(ChannelDTO newChannel) {
		return this.channelRepository.save(newChannel);
	}

	public boolean deleteChannel(Integer channelId) {
		if (!this.channelRepository.existsById(channelId))
			return false;
		this.channelRepository.deleteById(channelId);
		return true;
	}

	public Set<ChannelDTO> getChannelsByTag(Set<String> tags){
		return this.channelRepository.findByTagsIn(tags);
	}
	
}
