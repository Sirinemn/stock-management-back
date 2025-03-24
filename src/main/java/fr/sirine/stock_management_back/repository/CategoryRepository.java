package fr.sirine.stock_management_back.repository;

import fr.sirine.stock_management_back.entities.Category;
import fr.sirine.stock_management_back.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findByName(String name);
    List<Category> findByGroup(Group group);
}
