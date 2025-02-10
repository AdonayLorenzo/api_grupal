package api.web.repo;

import api.web.entity.Storyboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StoryboardRepo extends JpaRepository<Storyboard, Long> {

}