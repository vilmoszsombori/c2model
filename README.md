# Transformation of C code to Matlab/Simulink models

Developed by __Vilmos Zsombori__ while at _DaimlerChrysler SIM Technology Co., Ltd._ in Shanghai.  

## Motivation

In the modern development process of automotive embedded software, the graphical executable specification is becoming to take the place of the textual specification and accepted by both the manufactures and suppliers. There are advantages such as easy understanding and communication, fast development with executable specification in modeling and simulation tools, and automatic code generation since the commercial tools become more and more mature.

The overall aim of this project is to transform the existing control functions in C code into the block diagram models in Matlab/Simulink. Considering amount of source code to be transformed, the necessity of unified transformation and the existence of strict transformation patterns based on the C/C++ language structures motivated to explore the possibility of developing a tool, which could assist the engineers in the transformation work. The approach presented in this paper is based on a third party parser generator called JavaCC and the application of the generator to develop Java-based processor for C/C++ grammar.


## Approach

A simulation model to be created in Simulink is stored in a textual file (Simulink model file “mdl”) that defines the whole structure of the target system. The goal is to develop a translator that processes the given C files and generates the correspondent Simulink model files automatically. Considering the possibility offered by Matlab, to build up Simulink models through the command line, a thorough understanding of the model file structure is not needed. We generate an intermediate Matlab Script file instead, of which execution in the Matlab command line results in the desired Simulink model.


## Restrictions

The usual C codes take advantage of commercial preprocessors to preprocess the source files before being inputs to analyzers. The directives defined in the preprocessor in C specify actions for (1) macro substitution, (2) conditional compilation and (3) source file inclusion. Only after a successful run of the preprocessor can be the source code headed for any kind of parsing. The developed parser assumes that the C source file has been preprocessed. 
