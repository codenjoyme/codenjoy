#ifndef ELEMENT_H
#define ELEMENT_H

#include "utils/utils.h"


class Element {
public:
	Element(Char el);
	Element(String name);

	bool operator==(const Element& el) const;

	Char getChar() const;
	String valueOf(Char ch) const;
	
private:
	static ElementMap initialiseElements();
	static const ElementMap Elements;

	ElementItem elem;
};

#endif