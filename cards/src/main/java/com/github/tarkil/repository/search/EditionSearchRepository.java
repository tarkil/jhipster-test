package com.github.tarkil.repository.search;

import com.github.tarkil.domain.Edition;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Edition entity.
 */
public interface EditionSearchRepository extends ElasticsearchRepository<Edition, Long> {
}
