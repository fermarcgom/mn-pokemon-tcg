package io.github.fermarcgom.persistence.dao;

import io.github.fermarcgom.dto.Type;
import io.github.fermarcgom.persistence.domain.Card;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaSpecificationExecutor;
import io.micronaut.data.jpa.repository.criteria.Specification;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.PageableRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface CardRepository extends PageableRepository<Card, Integer>, JpaSpecificationExecutor<Card> {
    Optional<Card> findByPokemonName(String pokemonName);

    default Page<Card> findAll(Pageable page, Type type) {
        return findAll(Specifications.withType(type), page);
    }

    class Specifications {
        public static Specification<Card> withType(Type type) {
            return (root, query, criteriaBuilder) -> {
                return type == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("pokemonType"), type.name());
            };
        }
    }
}
