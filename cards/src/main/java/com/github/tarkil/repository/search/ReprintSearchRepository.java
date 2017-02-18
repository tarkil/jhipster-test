package com.github.tarkil.repository.search;

import com.github.tarkil.domain.Reprint;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Reprint entity.
 */
public interface ReprintSearchRepository extends ElasticsearchRepository<Reprint, Long> {
}
