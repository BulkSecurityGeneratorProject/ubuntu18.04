package com.signzy.ubuntu.repository;

import com.signzy.ubuntu.domain.Directory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Directory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DirectoryRepository extends JpaRepository<Directory, Long>, JpaSpecificationExecutor<Directory> {

}
