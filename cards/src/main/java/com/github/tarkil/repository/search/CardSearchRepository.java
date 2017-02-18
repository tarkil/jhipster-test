package com.github.tarkil.repository.search;

import com.github.tarkil.domain.Card;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Card entity.
 */
public interface CardSearchRepository extends ElasticsearchRepository<Card, Long> {
}
