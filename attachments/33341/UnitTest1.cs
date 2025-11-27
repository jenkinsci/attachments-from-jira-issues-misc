using System;
using System.IO;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace TestMSTest
{
	[TestClass]
	public class UnitTest1
	{


		[TestMethod]
		[DataSource("Microsoft.VisualStudio.TestTools.DataSource.CSV", @"Nonexistentfolder\Nonexistentfile.csv", "PensionCreditDate#csv", DataAccessMethod.Sequential)]
		public void TestCreatesError()
		{

		}

		[TestMethod]
		public void TestPasses()
		{

		}
	}
}
