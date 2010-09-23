/*
 * Legal Notice
 *
 * This document and associated source code (the "Work") is a preliminary
 * version of a benchmark specification being developed by the TPC. The
 * Work is being made available to the public for review and comment only.
 * The TPC reserves all right, title, and interest to the Work as provided
 * under U.S. and international laws, including without limitation all patent
 * and trademark rights therein.
 *
 * No Warranty
 *
 * 1.1 TO THE MAXIMUM EXTENT PERMITTED BY APPLICABLE LAW, THE INFORMATION
 *     CONTAINED HEREIN IS PROVIDED "AS IS" AND WITH ALL FAULTS, AND THE
 *     AUTHORS AND DEVELOPERS OF THE WORK HEREBY DISCLAIM ALL OTHER
 *     WARRANTIES AND CONDITIONS, EITHER EXPRESS, IMPLIED OR STATUTORY,
 *     INCLUDING, BUT NOT LIMITED TO, ANY (IF ANY) IMPLIED WARRANTIES,
 *     DUTIES OR CONDITIONS OF MERCHANTABILITY, OF FITNESS FOR A PARTICULAR
 *     PURPOSE, OF ACCURACY OR COMPLETENESS OF RESPONSES, OF RESULTS, OF
 *     WORKMANLIKE EFFORT, OF LACK OF VIRUSES, AND OF LACK OF NEGLIGENCE.
 *     ALSO, THERE IS NO WARRANTY OR CONDITION OF TITLE, QUIET ENJOYMENT,
 *     QUIET POSSESSION, CORRESPONDENCE TO DESCRIPTION OR NON-INFRINGEMENT
 *     WITH REGARD TO THE WORK.
 * 1.2 IN NO EVENT WILL ANY AUTHOR OR DEVELOPER OF THE WORK BE LIABLE TO
 *     ANY OTHER PARTY FOR ANY DAMAGES, INCLUDING BUT NOT LIMITED TO THE
 *     COST OF PROCURING SUBSTITUTE GOODS OR SERVICES, LOST PROFITS, LOSS
 *     OF USE, LOSS OF DATA, OR ANY INCIDENTAL, CONSEQUENTIAL, DIRECT,
 *     INDIRECT, OR SPECIAL DAMAGES WHETHER UNDER CONTRACT, TORT, WARRANTY,
 *     OR OTHERWISE, ARISING IN ANY WAY OUT OF THIS OR ANY OTHER AGREEMENT
 *     RELATING TO THE WORK, WHETHER OR NOT SUCH AUTHOR OR DEVELOPER HAD
 *     ADVANCE NOTICE OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * Contributors
 * - Doug Johnson
 */

// 2006 Rilson Nascimento


/*
*	Database loader class for TRADE_TYPE table.
*/
#ifndef PGSQL_TRADE_TYPE_LOAD_H
#define PGSQL_TRADE_TYPE_LOAD_H

namespace TPCE
{

class CPGSQLTradeTypeLoad : public CPGSQLLoader <TRADE_TYPE_ROW>
{	
public:
	CPGSQLTradeTypeLoad(char *szHost, char *szDBName, char *szPostmasterPort, char *szTable = "TRADE_TYPE")
		: CPGSQLLoader<TRADE_TYPE_ROW>(szHost, szDBName, szPostmasterPort, szTable)
	{
	};


	virtual void WriteNextRecord(PT next_record)
	{
		CopyRow(next_record);	//copy to the bound location inside this class first
	
		buf.push_back(m_row.TT_ID);
		buf.push_back(m_row.TT_NAME);
		//buf.push_back((m_row.TT_IS_SELL ? "true" : "false"));
		buf.push_back(stringify(m_row.TT_IS_SELL));
		//buf.push_back((m_row.TT_IS_MRKT ? "true" : "false"));
		buf.push_back(stringify(m_row.TT_IS_MRKT));

		m_TW->insert(buf);
		buf.clear();
	}

};

}	// namespace TPCE

#endif //PGSQL_TRADE_TYPE_LOAD_H
