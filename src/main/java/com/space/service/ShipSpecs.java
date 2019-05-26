package com.space.service;

        import com.space.model.Ship;
        import com.space.model.ShipType;
        import org.springframework.data.jpa.domain.Specification;

        import javax.persistence.criteria.*;
        import java.util.Date;

/**
 * Specifications for a Ship
 */
public class ShipSpecs {
    private static Specification<Ship> containPartOfNameString(String partOfName) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.like(root.get("name"), '%' + partOfName + '%');
        };
    }

    private static Specification<Ship> containPartOfPlanetString(String partOfPlanet) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.like(root.get("planet"), '%' + partOfPlanet + '%');
        };
    }

    private static Specification<Ship> betweenDates(Long after, Long before) {
        if (after != null && before != null) {
            return (root, query, criteriaBuilder) -> {
                return criteriaBuilder.between(root.get("prodDate"), new Date(after), new Date(before));
            };
        } else if (after != null) {
            return (root, query, criteriaBuilder) -> {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("prodDate"), new Date(after));
            };
        } else {
            return (root, query, criteriaBuilder) -> {
                return criteriaBuilder.lessThanOrEqualTo(root.get("prodDate"), new Date(before));
            };
        }
    }

    private static Specification<Ship> betweenCrewSize(Integer minCrewSize, Integer maxCrewSize) {
        if (minCrewSize != null && maxCrewSize != null) {
            return (root, query, criteriaBuilder) -> {
                return criteriaBuilder.between(root.get("crewSize"), minCrewSize, maxCrewSize);
            };
        } else if (minCrewSize != null) {
            return (root, query, criteriaBuilder) -> {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("crewSize"), minCrewSize);
            };
        } else {
            return (root, query, criteriaBuilder) -> {
                return criteriaBuilder.lessThanOrEqualTo(root.get("crewSize"), maxCrewSize);
            };
        }
    }

    private static Specification<Ship> betweenDouble(Double min, Double max, String attribute) {
        if (min != null && max != null) {
            return (root, query, criteriaBuilder) -> {
                return criteriaBuilder.between(root.get(attribute), min, max);
            };
        } else if (min != null) {
            return (root, query, criteriaBuilder) -> {
                return criteriaBuilder.greaterThanOrEqualTo(root.get(attribute), min);
            };
        } else {
            return (root, query, criteriaBuilder) -> {
                return criteriaBuilder.lessThanOrEqualTo(root.get(attribute), max);
            };
        }
    }

    private static Specification<Ship> withShipType(ShipType shipType) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("shipType"), shipType);
        };
    }

    private static Specification<Ship> withUsed(Boolean isUsed) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("isUsed"), isUsed);
        };
    }


    public static Specification<Ship> getSpecs(String name, String planet, Long after, Long before,
                                               Integer minCrewSize, Integer maxCrewSize,
                                               Double minSpeed, Double maxSpeed,
                                               Double minRating, Double maxRating,
                                               ShipType shipType, Boolean isUsed) {
        Specification<Ship> result = new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
        };
        if (name != null) {
            result = result.and(containPartOfNameString(name));
        }
        if (planet != null) {
            result = result.and(containPartOfPlanetString(planet));
        }
        if (after != null || before != null) {
            result = result.and(betweenDates(after, before));
        }
        if (minCrewSize != null || maxCrewSize != null) {
            result = result.and(betweenCrewSize(minCrewSize, maxCrewSize));
        }
        if (minSpeed != null || maxSpeed != null) {
            result = result.and(betweenDouble(minSpeed, maxSpeed, "speed"));
        }
        if (minRating != null || maxRating != null) {
            result = result.and(betweenDouble(minRating, maxRating, "rating"));
        }
        if (shipType != null) {
            result = result.and(withShipType(shipType));
        }
        if (isUsed != null) {
            result = result.and(withUsed(isUsed));
        }
        return result;
    }
}
