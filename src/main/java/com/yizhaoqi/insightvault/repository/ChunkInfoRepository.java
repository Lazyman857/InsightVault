package com.yizhaoqi.insightvault.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yizhaoqi.insightvault.model.ChunkInfo;

import java.util.List;

public interface ChunkInfoRepository extends JpaRepository<ChunkInfo, Long> {
    List<ChunkInfo> findByFileMd5OrderByChunkIndexAsc(String fileMd5);
}
