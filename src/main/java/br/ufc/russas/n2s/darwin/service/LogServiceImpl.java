package br.ufc.russas.n2s.darwin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufc.russas.n2s.darwin.dao.LogDAOIfc;
import br.ufc.russas.n2s.darwin.model.Log;

@Service("logServiceIfc")
@Transactional
public class LogServiceImpl implements LogServiceIfc {

	private LogDAOIfc logDAOIfc;
	
	public LogDAOIfc getLogDAOIfc() {
        return logDAOIfc;
    }
    @Autowired(required = true)
    public void setLogDAOIfc(@Qualifier("logDAOIfc")LogDAOIfc logDAOIfc) {
        this.logDAOIfc = logDAOIfc;
    }
	
	@Override
	public void adicionaLog(Log log) {
		this.getLogDAOIfc().adicionaLog(log);
	}

	
}
