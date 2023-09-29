`include "control_unit.v"

module cu_tb;
  reg[5:0] opcode, funct;
  wire memtoreg, memwrite, branch, alusrc, regdst, regwrite;
  wire[2:0] alucontrol;
  reg [31:0] mem[0:31];
  control_unit cu1(opcode, funct, memtoreg, memwrite, branch, alusrc, regdst, regwrite, alucontrol);
  initial begin
    $monitor("%5b\t%5b\t%1b%1b%1b%1b%1b%1b\t%3b", opcode, funct, regwrite, regdst, alusrc, branch, memwrite, memtoreg, alucontrol);
    opcode=0;
    funct=0;
    
    
    #5 opcode=6'b000000;
    funct=0;
    #5 funct=6'b100000;
    #5 funct=6'b100010;
    #5 funct=6'b100100;
    #5 funct=6'b100101;
    #5 funct=6'b101010;
    #5 funct=6'b111111;
    
    #5 $display("\n");

    #5 opcode=6'b001000;
    funct=0;
    #5 funct=6'b100000;
    #5 funct=6'b100010;
    #5 funct=6'b100100;
    #5 funct=6'b100101;
    #5 funct=6'b101010;
    #5 funct=6'b111111;
    
    #5 $display("\n");
    
    #5 opcode=6'b100011;
    funct=0;
    #5 funct=6'b100000;
    #5 funct=6'b100010;
    #5 funct=6'b100100;
    #5 funct=6'b100101;
    #5 funct=6'b101010;
    #5 funct=6'b111111;

    #5 $display("\n");
    
    #5 opcode=6'b101011;
    funct=0;
    #5 funct=6'b100000;
    #5 funct=6'b100010;
    #5 funct=6'b100100;
    #5 funct=6'b100101;
    #5 funct=6'b101010;
    #5 funct=6'b111111;
    
    #5 $display("\n");

    #5 opcode=6'b000100;
    funct=0;
    #5 funct=6'b100000;
    #5 funct=6'b100010;
    #5 funct=6'b100100;
    #5 funct=6'b100101;
    #5 funct=6'b101010;
    #5 funct=6'b111111;

    #5 $display("\n");
    
    #5 opcode=6'b111111;
    funct=0;
    #5 funct=6'b100000;
    #5 funct=6'b100010;
    #5 funct=6'b100100;
    #5 funct=6'b100101;
    #5 funct=6'b101010;
    #5 funct=6'b111111;
  end
endmodule