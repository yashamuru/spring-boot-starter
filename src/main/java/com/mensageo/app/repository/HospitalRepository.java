package com.mensageo.app.repository;

import com.mensageo.app.model.Hospital;
import org.springframework.data.repository.CrudRepository;

public interface HospitalRepository  extends CrudRepository<Hospital, Long> {
}
