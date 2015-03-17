package com.hybris.dao;


import com.hybris.dao.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuestDao extends JpaRepository<Guest, Long> {

    void delete(Guest deleted);

    List<Guest> findAllByOrderByCreatedAsc();

    Guest getOne(Long id);

    Guest save(Guest guest);

    List<Guest> findByFirstnameAndLastname(String firstname, String lastname);
}
