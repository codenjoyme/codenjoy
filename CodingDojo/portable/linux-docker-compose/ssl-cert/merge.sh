#!/usr/bin/env bash
rm STAR_domain_com_CHAIN.crt

cat STAR_domain_com.crt >> STAR_domain_com_CHAIN.crt
cat SectigoRSADomainValidationSecureServerCA.crt >> STAR_domain_com_CHAIN.crt
cat USERTrustRSAAddTrustCA.crt >> STAR_domain_com_CHAIN.crt
cat AddTrustExternalCARoot.crt >> STAR_domain_com_CHAIN.crt
