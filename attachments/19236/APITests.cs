using System;
using System.Collections.Generic;
using System.Text;
using NUnit.Framework;

namespace ModelCenterTest
{
   [TestFixture]
   class APITests
   {
      private MSScriptControl.IScriptControl sc;

      [SetUp]
      public void SetUp()
      {
         sc = new MSScriptControl.ScriptControl();
      }

      [TearDown]
      public void TearDown()
      {
         if (sc != null)
         {
            sc.Reset();
            System.Runtime.InteropServices.Marshal.FinalReleaseComObject(sc);
         }
         sc = null;
      }

      [Test, TestCaseSource("testList")]
      public void APITest(string file)
      {
         
         sc.Language = "VBScript";
         string rpath = file.Substring(0, file.LastIndexOf('\\'));
         sc.ExecuteStatement("TestRunnerPath=\"" + rpath + "\"");
         string code = System.IO.File.ReadAllText(file);
         sc.Timeout = 300000;
         try
         {
            sc.AddCode(code);
         }
         catch (Exception e)
         {
            string msg = e.ToString() + "\n" +
               "Description: " + sc.Error.Description + "\n" +
               "Source: " + sc.Error.Source + "\n" +
               "Line: " + sc.Error.Line + "\n" +
               "Text: " + sc.Error.Text + "\n" +
               "Number: " + sc.Error.Number;
            Assert.Fail(msg);
         }
      }

      static public IEnumerable<TestCaseData> testList
      {
         get
         {
            commonfns func=new commonfns();
            string testDir = func.getActualDirectoryPath("..\\..\\..\\TestCases\\AutoTests");
            IEnumerable<string> files = System.IO.Directory.GetFiles(testDir, "*.vbs");
            foreach (string f in files)
            {
               string testName = f.Substring(f.LastIndexOf('\\')+1);
               testName = testName.Substring(0, testName.Length - 4);
               yield return new TestCaseData(f).SetName("APITests." + testName);
            }
         }
      }
   }
}
