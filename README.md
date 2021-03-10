# InterlockLedger REST Client for Java - JsonStore InterlockApp Support
Copyright (c) 2021 InterlockLedger Network

This package is JsonStore InterlockApp support for the java client to the InterlockLedger Node REST API. 
It encodes/decodes JsonStore structures sent/returned to/from an InterlockLedger Node (and chains) enabled for the JsonStore InterlockApp.

## The InterlockLedger

An InterlockLedger network is a peer-to-peer network of nodes. Each node runs the
InterlockLedger software. All communication between nodes is point-to-point and digitally
signed, but not mandatorily encrypted. This means that data is shared either publicly or on
a need-to-know basis, depending on the application.

In the InterlockLedger, the ledger is composed of myriads of independently permissioned 
chains, comprised of blockchained records of data, under the control of their owners, but
that are tied by Interlockings, that avoid them having their content/history being rewritten
even by their owners. For each network the ledger is the sum of all chains in the
participating nodes.

A chain is a sequential list of records, back chained with signatures/hashes to the previous 
records, so that no changes in them can go undetected. A record is tied to some enabled 
Application, that defines the metadata associate with it, and the constraints defined in 
this public metadata, forcibly stored in the network genesis chain, is akin to validation 
that each correct implementation of the node software is able to enforce, but more 
importantly, any external logic can validate the multiple dimensions of validity for records/
chains/interlockings/the ledger.

## Usage

### Dependencies

This client requires the **JAX-RS client API** thus you will need to add a **JAX-RS**
compliant implementation to your project in order to use this library.

### Examples

To get examples about how to use this library, see the unit-tests provided with this
code.

## License

This library is released under a **BSD 3-Clause License**.
