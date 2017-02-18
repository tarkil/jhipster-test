package com.github.tarkil.repository.search;

import com.github.tarkil.domain.Block;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Block entity.
 */
public interface BlockSearchRepository extends ElasticsearchRepository<Block, Long> {
}
