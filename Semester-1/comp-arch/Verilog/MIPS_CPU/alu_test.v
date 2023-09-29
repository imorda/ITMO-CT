`include "alu.v"

module alu_tb;
  reg signed [31: 0] srca, srcb;
  reg[2: 0] alucontrol;
  
  wire signed [31: 0] result;
  wire zero;
  
  alu alu1(srca, srcb, alucontrol, result, zero);
  
  always begin
    #5 alucontrol = alucontrol + 1;
  end
  
  integer seed = 536977;
  initial begin
    srca = $random(seed);
    srcb = $random(seed);
    

    $display("\t%32b\t%d\n\t%32b\t%d\n", srca, srca, srcb, srcb);
    alucontrol = 0;
    $monitor("%3b:\t%32b\t%b\t%d", alucontrol, result, zero, result);
    #(5*8-1) $finish(0);
  end
endmodule