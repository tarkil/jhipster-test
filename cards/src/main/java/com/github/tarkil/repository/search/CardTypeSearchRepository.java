package com.github.tarkil.repository.search;

import com.github.tarkil.domain.CardType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CardType entity.
 */
public interface CardTypeSearchRepository extends ElasticsearchRepository<CardType, Long> {
}
