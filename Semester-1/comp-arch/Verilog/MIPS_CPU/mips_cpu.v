`include "alu.v"
`include "control_unit.v"
`include "util.v"

module mips_cpu(clk, instruction_memory_a, instruction_memory_rd, data_memory_a, data_memory_rd, data_memory_we, data_memory_wd,
                register_a1, register_a2, register_a3, register_we3, register_wd3, register_rd1, register_rd2);
  input clk;
  output wire data_memory_we;
  output wire [31:0] instruction_memory_a, data_memory_a, data_memory_wd;
  inout [31:0] instruction_memory_rd, data_memory_rd;
  output wire register_we3;
  output wire [4:0] register_a1, register_a2, register_a3;
  output wire [31:0] register_wd3;
  inout [31:0] register_rd1, register_rd2;
  
  assign register_a1 = instruction_memory_rd[25:21];
  assign register_a2 = instruction_memory_rd[20:16];
  
  wire[4:0] WriteRegRes;
  mux2_5 WriteReg(instruction_memory_rd[20:16], instruction_memory_rd[15:11], RegDst, WriteRegRes);
  assign register_a3 = WriteRegRes;

  wire[31:0] pc_new;
  wire[31:0] pc;
  d_flop pcs(pc_new, clk, pc);
  
  wire PCSrc = Branch & Zero;
  wire MemToReg;
  wire MemWrite;
  wire Branch;
  wire[2:0] ALUControl;
  wire ALUSrc;
  wire RegDst;
  wire RegWrite;
  control_unit cu(Op, Funct, MemToReg, MemWrite, Branch, ALUSrc, RegDst, RegWrite, ALUControl);
  
  assign data_memory_we = MemWrite;
  assign register_we3 = RegWrite;
 
  wire[5:0] Op = instruction_memory_rd[31:26];
  wire[5:0] Funct = instruction_memory_rd[5:0];
 
  mux2_32 pc_mux(pcinc4, PCBranchRes, PCSrc, pc_new);
 
  
  wire[31:0] pcinc4;
  adder PCPlus4(pc, 4, pcinc4);
  
  wire[31:0] SignImmRes;
  sign_extend SignImm(instruction_memory_rd[15:0], SignImmRes);
  
  wire[31:0] SignImmShRes;
  shl_2 sh(SignImmRes, SignImmShRes);
  wire[31:0] PCBranchRes;
  adder PCBranch(SignImmShRes, pcinc4, PCBranchRes);
  
  wire[31:0] srcBRes;
  mux2_32 SrcB(register_rd2, SignImmRes, ALUSrc, srcBRes);
  
  
  wire Zero;
  wire[31:0] ALUResult;
  alu alu1(register_rd1, srcBRes, ALUControl, ALUResult, Zero);
  
  assign data_memory_a = ALUResult;
  mux2_32 MemToRegMux(ALUResult, data_memory_rd, MemToReg, register_wd3);
  
  assign instruction_memory_a=pc;
  
  assign data_memory_wd = register_rd2;
endmodule
