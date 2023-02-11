package br.ufsm.csi.poow2.spring_rest.repository;

import br.ufsm.csi.poow2.spring_rest.model.entidades.PilaCoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PilaCoinRepository extends JpaRepository<PilaCoin,Long> {

    @Query("select p from PilaCoin p where p.chaveCriador = :chave")
    public PilaCoin pilaByKeyOwner(byte[] chave);
}
