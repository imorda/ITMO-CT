module not_switch(in, out);
  input in;
  output out;
  
  supply1 power;
  supply0 ground;
  
  pmos p1(out, power, in);  //drain, source, gate
  nmos n1(out, ground, in); //drain, source, gate
endmodule

module nand_switch(a, b, out);
  input a, b;
  output out;
  
  supply1 power;
  supply0 ground;
  
  wire w;
  
  nmos n1(w, ground, b);
  nmos n1(out, w, a);
  pmos p1(out, power, a); //выход, вход, управляющий
  pmos p2(out, power, b);
endmodule

module and_switch(a, b, out);
  input a, b;
  output out;
  
  wire w;
  
  nand_switch nand1(a, b, w);
  not_switch not1(w, out);
endmodule

module nor_switch(a, b, out);
  input a, b;
  output out;
  
  supply1 power;
  supply0 ground;
  
  wire w;
  
  pmos p1(w, power, b);
  pmos p1(out, w, a);
  nmos n1(out, ground, a); //выход, вход, управляющий
  nmos n2(out, ground, b);
endmodule

module or_switch(a, b, out);
  input a, b;
  output out;
  
  wire w;
  
  nor_switch nor1(a, b, w);
  not_switch not1(w, out);
endmodule

module median_switch(a, b, c, out);
  input a, b, c;
  output out;
  wire w1, w2, w3;
  or_switch or1(b, c, w1);
  and_switch and1(a, w1, w2);
  and_switch and2(b, c, w3);
  or_switch or2(w2, w3, out);
endmodule

module xor_switch(a, b, out);
  input a, b;
  output out;
  wire w0, w1, w2, w3;
  
  not_switch not0(a, w0);
  not_switch not1(b, w1);

  and_switch and0(w0, b, w2);
  and_switch and1(a, w1, w3);
  
  or_switch or0(w2, w3, out);
endmodule

module sum_bit(x, y, c0, z, c1);
  input x, y, c0;
  output z, c1;
  wire w0, w1, w2;
  
  xor_switch xor0(x, y, w0);
  xor_switch xor1(w0, c0, z);
  
  and_switch and0(w0, c0, w1);
  and_switch and1(x, y, w2);
  
  or_switch or0(w1, w2, c1);
endmodule

module ternary_max(a0, a1, b0, b1, out0, out1);
  input a0, a1, b0, b1;
  output out0, out1;
  wire w0, w1;
  
  or_switch or0(a1, b1, out1);
  
  or_switch or1(a0, b0, w0);
  not_switch not0(out1, w1);
  and_switch and0(w0, w1, out0);
endmodule

module ternary_min(a0, a1, b0, b1, out0, out1);
  input a0, a1, b0, b1;
  output out0, out1;
  wire w0, w1, w2, w3;

  and_switch and0(a1, b1, out1);
  
  or_switch or0(a1, a0, w0);
  or_switch or1(b1, b0, w1);
  and_switch and1(w0, w1, w2);
  
  not_switch not0(out1, w3);
  and_switch and2(w3, w2, out0);
endmodule

module ternary_consensus(a0, a1, b0, b1, out0, out1);
  input a0, a1, b0, b1;
  output out0, out1;
  wire w0, w1, w2, w3;
  
  and_switch and0(a1, b1, out1);
  
  or_switch or0(a1, a0, w1);
  or_switch or1(w1, b1, w2);
  or_switch or2(w2, b0, w3);
  
  not_switch not0(out1, w0);
  and_switch and1(w0, w3, out0);
endmodule

module ternary_any(a0, a1, b0, b1, out0, out1);
  input a0, a1, b0, b1;
  output out0, out1;
  wire w0, w1, w2, w3, w4, w5, w6, w7, w8, w9;
  
  and_switch and0(a0, b0, w0);
  
  or_switch or0(a1, a0, w8);
  not_switch not0(w8, w1);
  and_switch and1(b1, w1, w2);
  
  or_switch or1(b1, b0, w9);
  not_switch not1(w9, w3);
  and_switch and2(a1, w3, w4);
  
  or_switch or2(w0, w2, w5);
  or_switch or3(w5, w4, out0);
  
  
  or_switch or4(a1, b1, w6);
  not_switch not2(out0, w7);
  and_switch and3(w6, w7, out1);
endmodule