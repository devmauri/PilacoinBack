package br.ufsm.csi.poow2.spring_rest.repository;

import br.ufsm.csi.poow2.spring_rest.model.entidades.Bloco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BlocoRepository extends JpaRepository<Bloco,Long> {

    @Query("select b from Bloco b where b.chaveUsuarioMinerador = :chave")
    public Bloco blocoByKey(byte[] chave);
}
