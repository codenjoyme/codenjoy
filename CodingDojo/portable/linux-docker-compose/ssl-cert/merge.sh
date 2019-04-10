#!/usr/bin/env bash
rm domain_CHAIN.crt

cat domain.crt >> domain_CHAIN.crt
cat SectigoRSADomainValidationSecureServerCA.crt >> domain_CHAIN.crt
cat USERTrustRSAAddTrustCA.crt >> domain_CHAIN.crt
cat AddTrustExternalCARoot.crt >> domain_CHAIN.crt
